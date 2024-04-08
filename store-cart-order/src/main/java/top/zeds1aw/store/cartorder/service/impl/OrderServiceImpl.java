package top.zeds1aw.store.cartorder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.zeds1aw.store.cartorder.feign.ProductFeignClient;
import top.zeds1aw.store.cartorder.filter.UserInfoFilter;
import top.zeds1aw.store.cartorder.model.dao.CartMapper;
import top.zeds1aw.store.cartorder.model.dao.OrderItemMapper;
import top.zeds1aw.store.cartorder.model.dao.OrderMapper;
import top.zeds1aw.store.cartorder.model.pojo.Order;
import top.zeds1aw.store.cartorder.model.pojo.OrderItem;
import top.zeds1aw.store.cartorder.model.pojo.Product;
import top.zeds1aw.store.cartorder.model.request.CreateOrderReq;
import top.zeds1aw.store.cartorder.model.vo.CartVO;
import top.zeds1aw.store.cartorder.model.vo.OrderItemVO;
import top.zeds1aw.store.cartorder.model.vo.OrderVO;
import top.zeds1aw.store.cartorder.mq.MsgSender;
import top.zeds1aw.store.cartorder.service.CartService;
import top.zeds1aw.store.cartorder.service.OrderService;
import top.zeds1aw.store.cartorder.util.OrderCodeFactory;
import top.zeds1aw.store.common.common.Constant.Cart;
import top.zeds1aw.store.common.common.Constant.OrderStatusEnum;
import top.zeds1aw.store.common.common.Constant.SaleStatus;
import top.zeds1aw.store.common.exception.StoreException;
import top.zeds1aw.store.common.exception.StoreExceptionEnum;
import top.zeds1aw.store.common.util.QRCodeGenerator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：     订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Value("${file.upload.ip}")
    String ip;

    @Value("${file.upload.port}")
    String port;

    @Value("${file.upload.dir}")
    String FILE_UPLOAD_DIR;

    @Autowired
    MsgSender msgSender;

    //数据库事务
//    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public String create(CreateOrderReq createOrderReq) {

        //拿到用户ID
//        Integer userId = userFeignClient.getUser().getId();
        Integer userId = UserInfoFilter.userThreadLocal.get().getId();

        //从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Cart.SELECTED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        //如果购物车已勾选的为空，报错
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new StoreException(StoreExceptionEnum.CART_EMPTY);
        }
        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        //把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        //扣库存
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            Product product = productFeignClient.detailForFeign(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new StoreException(StoreExceptionEnum.NOT_ENOUGH);
            }
            productFeignClient.updateStock(product.getId(), stock);
        }
        //把购物车中的已勾选商品删除
        cleanCart(cartVOList);
        //生成订单
        Order order = new Order();
        //生成订单号，有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        //插入到Order表
        orderMapper.insertSelective(order);

        //循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    // 把购物车中的已勾选商品删除
    private void cleanCart(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    // 把购物车对象转化为订单商品对象
    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            Product product = productFeignClient.detailForFeign(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(SaleStatus.NOT_SALE)) {
                throw new StoreException(StoreExceptionEnum.NOT_SALE);
            }
            //判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new StoreException(StoreExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    // 订单详情
    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在，则报错
        if (order == null) {
            throw new StoreException(StoreExceptionEnum.NO_ORDER);
        }
        //订单存在，需要判断所属
//        Integer userId = userFeignClient.getUser().getId();
        Integer userId = UserInfoFilter.userThreadLocal.get().getId();
        if (!order.getUserId().equals(userId)) {
            throw new StoreException(StoreExceptionEnum.NOT_YOUR_ORDER);
        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        //获取订单对应的orderItemVOList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
//        Integer userId = userFeignClient.getUser().getId();
        Integer userId = UserInfoFilter.userThreadLocal.get().getId();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    @GlobalTransactional
    public void cancel(String orderNo, Boolean isFromSystem) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null) {
            throw new StoreException(StoreExceptionEnum.NO_ORDER);
        }
        //验证用户身份
        //订单存在，需要判断所属
        if (!isFromSystem) { //如果不是来自系统的定时任务批量取消订单，则进行用户身份校验
//            Integer userId = userFeignClient.getUser().getId();
            Integer userId = UserInfoFilter.userThreadLocal.get().getId();
            if (!order.getUserId().equals(userId)) {
                throw new StoreException(StoreExceptionEnum.NOT_YOUR_ORDER);
            }
        }

        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new StoreException(StoreExceptionEnum.WRONG_ORDER_STATUS);
        }
        //恢复商品库存
        //获取订单对应的orderItemList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            Product product = productFeignClient.detailForFeign(orderItem.getProductId());
            int stock = product.getStock() + orderItem.getQuantity();
//            productFeignClient.updateStock(orderItem.getProductId(), stock);
            msgSender.send(product.getId(), stock);
        }
    }

    // 生成二维码
    @Override
    public String qrcode(String orderNo) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String address = ip + ":" + port;
        String payUrl = "http://" + address + "/cart-order/pay?orderNo=" + orderNo;
        try {
            QRCodeGenerator
                    .generateQRCodeImage(payUrl, 350, 350,
                            FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://" + address + "/cart-order/images/" + orderNo + ".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null) {
            throw new StoreException(StoreExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus() == OrderStatusEnum.NOT_PAID.getCode()) {
            order.setOrderStatus(OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new StoreException(StoreExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    //发货
    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null) {
            throw new StoreException(StoreExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus() == OrderStatusEnum.PAID.getCode()) {
            order.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new StoreException(StoreExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null) {
            throw new StoreException(StoreExceptionEnum.NO_ORDER);
        }
        //如果是普通用户，就要校验订单的所属
        if (UserInfoFilter.userThreadLocal.get().getRole().equals(1) && !order.getUserId()
                .equals(UserInfoFilter.userThreadLocal.get().getId())) {
            throw new StoreException(StoreExceptionEnum.NOT_YOUR_ORDER);
        }
        //发货后可以完结订单
        if (order.getOrderStatus() == OrderStatusEnum.DELIVERED.getCode()) {
            order.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new StoreException(StoreExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    /* 获取未支付的订单 */
    public List<Order> getUnpaidOrders() {
        Date curTime = new Date();
        Date endTime = DateUtils.addDays(curTime, -1);
        Date begTime = DateUtils.addMinutes(endTime, -5);
        List<Order> unpaidOrders = orderMapper.selectUnpaidOrders(begTime, endTime);
        return  unpaidOrders;
    }
}
