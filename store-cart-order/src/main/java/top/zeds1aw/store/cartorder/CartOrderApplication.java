package top.zeds1aw.store.cartorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/*
描述：购物车订单模块启动类
 */
@SpringBootApplication
@MapperScan(basePackages = "top.zeds1aw.store.cartorder.model.dao")
//@EnableRedisHttpSession Redis共享Session依赖，升级Gateway网关后不再需要
@EnableFeignClients
@EnableEurekaClient
@ComponentScan({"top.zeds1aw.store.cartorder", "top.zeds1aw.store.common"})
public class CartOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartOrderApplication.class, args);
    }
}
