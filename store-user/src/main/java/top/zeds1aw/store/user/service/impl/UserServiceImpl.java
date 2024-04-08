package top.zeds1aw.store.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zeds1aw.store.common.exception.StoreException;
import top.zeds1aw.store.common.exception.StoreExceptionEnum;
import top.zeds1aw.store.common.util.MD5Utils;
import top.zeds1aw.store.user.model.dao.UserMapper;
import top.zeds1aw.store.user.model.pojo.User;
import top.zeds1aw.store.user.service.UserService;
import java.security.NoSuchAlgorithmException;

/**
 * 描述：     UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void register(String userName, String password) throws StoreException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new StoreException(StoreExceptionEnum.NAME_EXISTED);
        }

        //写到数据库，使用MD5加密进行数据保护
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new StoreException(StoreExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws StoreException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new StoreException(StoreExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws StoreException {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new StoreException(StoreExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        //1是普通用户，2是管理员，3是测试用户
        return user.getRole().equals(2);
    }
}
