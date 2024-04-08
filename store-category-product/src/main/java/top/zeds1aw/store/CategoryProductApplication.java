package top.zeds1aw.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
描述：商品分类与商品模块启动类
 */
@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
@EnableEurekaClient
@MapperScan(basePackages = "top.zeds1aw.store.categoryproduct.model.dao")
//@EnableRedisHttpSession Redis共享Session依赖，升级Gateway网关后不再需要
@ComponentScan({"top.zeds1aw.store.categoryproduct", "top.zeds1aw.store.common"})
public class CategoryProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(CategoryProductApplication.class, args);
    }
}
