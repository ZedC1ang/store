package top.zeds1aw.store.user.service;


import top.zeds1aw.store.user.model.pojo.User;
import top.zeds1aw.store.common.exception.StoreException;


/**
 * 描述：     UserService
 */
public interface UserService {

    /* 用户注册 */
    void register(String userName, String password) throws StoreException;

    /* 用户登录 */
    User login(String userName, String password) throws StoreException;

    /* 更新用户信息 */
    void updateInformation(User user) throws StoreException;

    /* 检测管理员身份 */
    boolean checkAdminRole(User user);
}
