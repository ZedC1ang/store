package top.zeds1aw.store.cartorder.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
描述：演示分布式锁
 */
@RestController
public class LockController {

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/redissonLock")
    public void redissonLock() throws InterruptedException {
        RLock redissonLock = redissonClient.getLock("redissonLock");
        boolean b = redissonLock.tryLock();
        if (b) {
            try {
                System.out.println("获取了分布式锁");
                Thread.sleep(5000);
            } finally {
                redissonLock.unlock();
                System.out.println("释放了分布式锁");
            }
        } else {
            System.out.println("没有获取到分布式锁");
        }
    }
}
