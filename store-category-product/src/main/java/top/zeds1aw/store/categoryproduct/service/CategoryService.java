package top.zeds1aw.store.categoryproduct.service;

import com.github.pagehelper.PageInfo;
import top.zeds1aw.store.categoryproduct.model.pojo.Category;
import top.zeds1aw.store.categoryproduct.model.request.AddCategoryReq;
import top.zeds1aw.store.categoryproduct.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：     分类目录Service
 */
public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
