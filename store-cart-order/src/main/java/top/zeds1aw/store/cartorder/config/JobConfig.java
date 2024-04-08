package top.zeds1aw.store.cartorder.config;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zeds1aw.store.cartorder.model.pojo.Order;
import top.zeds1aw.store.cartorder.service.OrderService;

import java.util.List;

/*
描述：定时任务类
 */
@Component
public class JobConfig {

    private final Logger log = LoggerFactory.getLogger(JobConfig.class);

    @Autowired
    OrderService orderService;

    @Autowired
    RedissonClient redissonClient;

    @Scheduled(cron = "0 0/5 * * * ?")
    /* 运用定时任务取消过期订单 */
    public void cancelUnpaidOrders() {
        /* 分布式锁 */
        RLock redissonLock = redissonClient.getLock("redissonLock");
        boolean b = redissonLock.tryLock();
        if (b) {
            try {
                System.out.println("获取了分布式锁");
                /* 业务逻辑 */
                List<Order> unpaidOrders = orderService.getUnpaidOrders();
                for (int i = 0; i < unpaidOrders.size(); i++) {
                    Order order = unpaidOrders.get(i);
                    orderService.cancel(order.getOrderNo(), true);
                }
            } finally {
                redissonLock.unlock();
                System.out.println("释放了分布式锁");
            }
        } else {
            System.out.println("没有获取到分布式锁");
        }


    }
}
