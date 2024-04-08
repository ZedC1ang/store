package top.zeds1aw.store.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
描述：启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@MapperScan(basePackages = "top.zeds1aw.store.user.model.dao")
//@EnableRedisHttpSession Redis共享Session依赖，升级Gateway网关后不再需要
@ComponentScan({"top.zeds1aw.store.user", "top.zeds1aw.store.common"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
