package com.djl.tacocloud.service;

import com.djl.tacocloud.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * @author djl
 * @create 2020/12/20 16:32
 */
@Service
public class JmsOrderMessageingService implements OrderMessagingService {

    JmsTemplate jmsTemplate;
    private final Destination orderQueue;

    @Qualifier("messageConverter")
    private MappingJackson2MessageConverter messageConverter;

    @Autowired
    public JmsOrderMessageingService(JmsTemplate jmsTemplate, Destination orderQueue) {
        this.jmsTemplate = jmsTemplate;
        this.orderQueue = orderQueue;
    }

    /**
     * sendOrder()方法调用了jms.send()，并传递了MessageCreator接口的一个匿名内部实现。这个实现类重写了createMessage()方法，从而能够通过给定的Order对象创建新的对象消息。
     *
     * @param order
     */
    @Override
    public void sendOrder(Order order) {
        // 默认情况下，将会使用SimpleMessageConverter，但是它需要被发送的对象实现Serializable。这种办法可能也不错，但有时候我们可能想要使用其他的消息转换器来消除这种限制，比如MappingJackson2MessageConverter。
        jmsTemplate.send(orderQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage(order);
                // 有一种更简单的方案，就是为消息添加一个自定义的头部，让它携带订单的来源。如果我们使用send()方法来发送taco订单，那么通过调用Message对象的setStringProperty()方法非常容易实现
                message.setStringProperty("x_order_source", "web");
                return message;
            }
        });
        // JmsTemplates的convertAndSend()方法简化了消息的发布，因为它不再需要MessageCreator。我们将要发送的对象直接传递给convertAndSend()，这个对象在发送之前会被转换成Message。
//        jmsTemplate.convertAndSend("xxx", order); // 与send()方法类似，convertAndSend()将会接受一个Destination对象或String值来确定目的地，我们也可以完全忽略目的地，将消息发送到默认目的地上。
        jmsTemplate.convertAndSend("xxx", order, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                // 还有一种方式能够在发送之前修改底层创建的Message对象。我们可以传递一个MessagePostProcessor作为convertAndSend()的最后一个参数，借助它我们可以在Message创建之后做任何想做的事情
                message.setStringProperty("x_order_source", "web");
                return message;
            }
        });
    }

    @Override
    public Order receiveOrder() {
        Message message = jmsTemplate.receive(orderQueue);

//        Order order = (Order) message;
        try {
            // 获取消息头属性值
            String orderSource = message.getStringProperty("x_order_source");
            Order order = (Order) messageConverter.fromMessage(message);
            return order;
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return null;
    }
}
