package top.zeds1aw.store.categoryproduct.service;

import com.github.pagehelper.PageInfo;
import top.zeds1aw.store.categoryproduct.model.pojo.Product;
import top.zeds1aw.store.categoryproduct.model.request.AddProductReq;
import top.zeds1aw.store.categoryproduct.model.request.ProductListReq;

/**
 * 描述：     商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);

    void updateStock(Integer productId, Integer stock);
}
