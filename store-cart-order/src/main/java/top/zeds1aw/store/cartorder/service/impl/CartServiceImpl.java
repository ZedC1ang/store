package top.zeds1aw.store.cartorder.service.impl;


import top.zeds1aw.store.cartorder.feign.ProductFeignClient;
import top.zeds1aw.store.cartorder.model.dao.CartMapper;
import top.zeds1aw.store.cartorder.model.pojo.Cart;
import top.zeds1aw.store.cartorder.model.pojo.Product;
import top.zeds1aw.store.cartorder.model.vo.CartVO;
import top.zeds1aw.store.cartorder.service.CartService;
import top.zeds1aw.store.common.common.Constant;
import top.zeds1aw.store.common.common.Constant.SaleStatus;
import top.zeds1aw.store.common.exception.StoreException;
import top.zeds1aw.store.common.exception.StoreExceptionEnum;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：     购物车Service实现类
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.SELECTED);
            cartMapper.insertSelective(cart);
        } else {
            //这个商品已经在购物车里了，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.SELECTED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    private void validProduct(Integer productId, Integer count) {
        Product product = productFeignClient.detailForFeign(productId);
        //判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(SaleStatus.NOT_SALE)) {
            throw new StoreException(StoreExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if (count > product.getStock()) {
            throw new StoreException(StoreExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，无法更新
            throw new StoreException(StoreExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则更新数量
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.SELECTED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，无法删除
            throw new StoreException(StoreExceptionEnum.DELETE_FAILED);
        } else {
            //这个商品已经在购物车里了，则可以删除
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，无法选择/不选中
            throw new StoreException(StoreExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则可以选中/不选中
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        //改变选中状态
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }
}
