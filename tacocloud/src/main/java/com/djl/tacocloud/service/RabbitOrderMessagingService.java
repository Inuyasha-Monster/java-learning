package com.djl.tacocloud.service;

import com.djl.tacocloud.entity.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

/**
 * @author djl
 * @create 2020/12/21 9:45
 */
public class RabbitOrderMessagingService implements OrderMessagingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendOrder(Order order) {
        final MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        final MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("x_order_source", "web");
        final Message message = messageConverter.toMessage(order, messageProperties);
        // 我们只指定了routing key（tacocloud.order）和消息本身，所以会使用默认的Exchange
        rabbitTemplate.send("tacocloud.order", message);
    }

    private void sendOrder2(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.order", order, message -> {
            final MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader("x_order_source", "web");
            return message;
        });
    }

    @Override
    public Order receiveOrder() {
        final Message message = rabbitTemplate.receive("tacocloud.order", 3000);
        if (message != null) {
            final MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
            final Order order = (Order) messageConverter.fromMessage(message);
            return order;
        }
        return null;
    }

    private Order receiveOrder2() {
        final Order xxxx = (Order) rabbitTemplate.receiveAndConvert("xxxx");

        // 要在receiveAndConvert()中使用ParameterizedTypeReference，消息转换器必须要实现SmartMessageConverter，目前Jackson2JsonMessageConverter是唯一一个可选的内置实现。
        final Order order = rabbitTemplate.receiveAndConvert("xxxx", new ParameterizedTypeReference<Order>() {
        });

        return xxxx;
    }
}
