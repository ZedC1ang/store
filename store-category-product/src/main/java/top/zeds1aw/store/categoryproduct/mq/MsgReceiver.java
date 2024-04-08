package top.zeds1aw.store.categoryproduct.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zeds1aw.store.categoryproduct.service.ProductService;

/*
描述：消费者 - 接收消息
 */
@Component
@RabbitListener(queues = "queue1")
public class MsgReceiver {

    @Autowired
    ProductService productService;

    @RabbitHandler
    public void process(String message) {
        System.out.println("收到消息"+message);
        String[] split = message.split(",");
        productService.updateStock(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

}
