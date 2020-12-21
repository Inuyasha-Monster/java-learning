package com.djl.tacocloud.service;

import com.djl.tacocloud.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author djl
 * @create 2020/12/21 10:04
 * KafkaTemplate与JmsTemplate和RabbitTemplate另一个不同之处在于它没有提供接收消息的方法。这意味着在Spring中想要消费来自Kafka主题的消息只有一种办法，就是编写消息监听器。
 */
public class KafkaMessagingService implements OrderMessagingService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void sendOrder(Order order) {
        kafkaTemplate.send("xxx", order);
    }

    /**
     * 我们就可以调用sendDefault()而不是send()了，这样可以不用指定主题的名称：
     * @param order
     */
    public void sendOrder2(Order order) {
        kafkaTemplate.sendDefault(order);
    }

    @Override
    public Order receiveOrder() {
        return null;
    }
}
