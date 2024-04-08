package top.zeds1aw.store.cartorder.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
描述：Redisson分布式锁配置类
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient config() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);
        return client;
    }
}

