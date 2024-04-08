package top.zeds1aw.store.categoryproduct.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.PostMapping;
import top.zeds1aw.store.categoryproduct.model.pojo.Product;
import top.zeds1aw.store.categoryproduct.model.request.ProductListReq;
import top.zeds1aw.store.categoryproduct.service.ProductService;
import top.zeds1aw.store.common.common.ApiRestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     前台商品Controller
 */
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @ApiOperation("商品详情")
    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("商品列表")
    @GetMapping("/product/list")
    public ApiRestResponse list(ProductListReq productListReq) {
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }

    @ApiOperation("内部模块调用方法：查看商品详情")
    @GetMapping("/product/detailForFeign")
    public Product detailForFeign(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return product;
    }

    @ApiOperation("内部模块调用方法：更新商品库存")
    @PostMapping("/product/updateStock")
    public void updateStock(@RequestParam Integer productId, @RequestParam Integer stock) {
        productService.updateStock(productId, stock);
    }

}
