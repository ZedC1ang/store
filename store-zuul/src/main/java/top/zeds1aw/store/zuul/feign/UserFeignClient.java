package top.zeds1aw.store.zuul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zeds1aw.store.user.model.pojo.User;

/*
描述：UserFeignCLient
 */
@FeignClient(value = "store-user")
public interface UserFeignClient {
    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @PostMapping("/checkAdminRole")
    public Boolean checkAdminRole(@RequestBody User user);
}
