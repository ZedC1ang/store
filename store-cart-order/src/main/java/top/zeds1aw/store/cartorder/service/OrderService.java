package top.zeds1aw.store.cartorder.service;

import com.github.pagehelper.PageInfo;
import top.zeds1aw.store.cartorder.model.pojo.Order;
import top.zeds1aw.store.cartorder.model.request.CreateOrderReq;
import top.zeds1aw.store.cartorder.model.vo.OrderVO;

import java.util.List;

/**
 * 描述：     订单Service
 */
public interface OrderService {


    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo, Boolean isFromSystem);

    String qrcode(String orderNo);

    void pay(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void deliver(String orderNo);

    void finish(String orderNo);

    /* 获取未支付的订单 */
    List<Order> getUnpaidOrders();
}
