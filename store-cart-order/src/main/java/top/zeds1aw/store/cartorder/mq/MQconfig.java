package top.zeds1aw.store.cartorder.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
描述：rabbitmq配置类
 */
@Configuration
public class MQconfig {
    @Bean
    public Queue queue1() {
        return new Queue("queue1");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("cloudExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queue1, DirectExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange).with("productStock");
    }
}
