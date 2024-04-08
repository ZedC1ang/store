package top.zeds1aw.store.cartorder.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zeds1aw.store.cartorder.filter.UserInfoFilter;
import top.zeds1aw.store.cartorder.model.vo.CartVO;
import top.zeds1aw.store.cartorder.service.CartService;
import top.zeds1aw.store.common.common.ApiRestResponse;

import java.util.List;

/**
 * 描述：     购物车Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

//    @Autowired
//    UserFeignClient userFeignClient;

    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list() {
        //内部获取用户ID，防止横向越权
//        List<CartVO> cartList = cartService.list(userFeignClient.getUser().getId());
        List<CartVO> cartList = cartService.list(UserInfoFilter.userThreadLocal.get().getId());
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
//        List<CartVO> cartVOList = cartService.add(userFeignClient.getUser().getId(), productId, count);
        List<CartVO> cartVOList = cartService.add(UserInfoFilter.userThreadLocal.get().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
//        List<CartVO> cartVOList = cartService.update(userFeignClient.getUser().getId(), productId, count);
        List<CartVO> cartVOList = cartService.update(UserInfoFilter.userThreadLocal.get().getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        //不能传入userID，cartID，否则可以删除别人的购物车
//        List<CartVO> cartVOList = cartService.delete(userFeignClient.getUser().getId(), productId);
        List<CartVO> cartVOList = cartService.delete(UserInfoFilter.userThreadLocal.get().getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/select")
    @ApiOperation("选择/不选择购物车的某商品")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        //不能传入userID，cartID，否则可以删除别人的购物车
//        List<CartVO> cartVOList = cartService.selectOrNot(userFeignClient.getUser().getId(), productId, selected);
        List<CartVO> cartVOList = cartService.selectOrNot(UserInfoFilter.userThreadLocal.get().getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/selectAll")
    @ApiOperation("全选择/全不选择购物车的某商品")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        //不能传入userID，cartID，否则可以删除别人的购物车
//        List<CartVO> cartVOList = cartService.selectAllOrNot(userFeignClient.getUser().getId(), selected);
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserInfoFilter.userThreadLocal.get().getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
