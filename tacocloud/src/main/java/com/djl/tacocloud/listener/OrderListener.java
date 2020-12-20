package com.djl.tacocloud.listener;

import com.djl.tacocloud.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author djl
 * @create 2020/12/20 17:05
 * receiveOrder()方法使用了JmsListener注解，这样它就会监听tacocloud.order.queue目的地的消息。该方法不需要使用JmsTemplate，也不会被你的应用显式调用。相反，Spring中的框架代码会等待消息抵达指定的目的地，当消息到达时，receiveOrder()方法会被自动调用，并且会将消息中的Order载荷作为参数。
 */
@Slf4j
@Component
public class OrderListener {

    @JmsListener(destination = "tacocloud.order.queue")
    private void receiveOrder(Order order) {
        log.debug(order.toString());
    }
}
