package top.zeds1aw.store.cartorder.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zeds1aw.store.cartorder.model.pojo.Product;

/*
描述：商品的FeignClient
 */
@FeignClient(value = "store-category-product")
public interface ProductFeignClient {
    @GetMapping("/product/detailForFeign")
    Product detailForFeign(@RequestParam Integer id);

    @PostMapping("/product/updateStock")
    void updateStock(@RequestParam Integer productId, @RequestParam Integer stock);
}
