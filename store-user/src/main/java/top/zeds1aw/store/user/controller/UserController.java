package top.zeds1aw.store.user.controller;

import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.zeds1aw.store.common.common.ApiRestResponse;
import top.zeds1aw.store.common.common.Constant;
import top.zeds1aw.store.common.exception.StoreException;
import top.zeds1aw.store.common.exception.StoreExceptionEnum;
import top.zeds1aw.store.user.filter.UserInfoFilter;
import top.zeds1aw.store.user.model.pojo.User;
import top.zeds1aw.store.user.service.UserService;

import java.util.Date;

/**
 * 描述：     用户控制器
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 注册
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password) throws StoreException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(StoreExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName, password);
        return ApiRestResponse.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password, HttpSession session)
            throws StoreException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //保存用户信息时，不保存密码
        user.setPassword(null);
        session.setAttribute(Constant.ZEDS1AW_STORE_USER, user);

        // 生成JWT
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create().withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ROLE, user.getRole())
                // 设置过期时间：2年
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);

        return ApiRestResponse.success(token);
    }

    /**
     * 更新个性签名
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature)
            throws StoreException {
        // 架构初期使用的Redis共享Session依赖，升级Gateway网关后无法使用Session
//        User currentUser = (User) session.getAttribute(Constant.ZEDS1AW_STORE_USER);
//        if (currentUser == null) {
//            return ApiRestResponse.error(StoreExceptionEnum.NEED_LOGIN);
//        }
        User currentUser = UserInfoFilter.userThreadLocal.get();
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出，清除session
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.ZEDS1AW_STORE_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录接口
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password, HttpSession session)
            throws StoreException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //校验是否是管理员
        if (userService.checkAdminRole(user)) {
            //是管理员，执行操作
            //保存用户信息时，不保存密码
            user.setPassword(null);
            session.setAttribute(Constant.ZEDS1AW_STORE_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(StoreExceptionEnum.NEED_ADMIN);
        }
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @PostMapping("/checkAdminRole")
    @ResponseBody
    public Boolean checkAdminRole(@RequestBody User user) {
        return userService.checkAdminRole(user);
    }

    /**
     * 模块间内部调用方法，获取当前登陆的User对象，为了避免暴露用户信息，可以在filter中对getUser进行拦截
     * @param session
     * @return
     */
    @GetMapping("/getUser")
    @ResponseBody
    public User getUser(HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.ZEDS1AW_STORE_USER);
        return currentUser;
    }


}
