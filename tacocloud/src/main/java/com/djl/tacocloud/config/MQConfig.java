package com.djl.tacocloud.config;

import com.djl.tacocloud.entity.Order;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * @author djl
 * @create 2020/12/20 16:37
 */
@Configuration
public class MQConfig {
    @Bean
    public Destination orderQueue() {
//        这里的ActiveMQQueue来源于Artemis（来自org.apache.activemq.artemis.jms.client包）
        // 如果选择使用ActiveMQ（而不是Artemis），那么同样有一个名为ActiveMQQueue的类（来自org.apache.activemq.command包）。
        return new ActiveMQQueue("tacocloud.order.queue");
    }

    @Bean("messageConverter")
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        //需要注意，在返回之前，我们调用了MappingJackson2MessageConverter的setTypeIdPropertyName()方法。这非常重要，因为这样能够让接收者知道传入的消息要转换成什么类型。默认情况下，它将会包含要转换的类型的全限定类名
        messageConverter.setTypeIdPropertyName("_typeId");
        return messageConverter;
    }


    /**
     * 这样的话，消息的_typeId属性中就不用发送全限定类型了，而是会发送order值。在接收端的应用中，将会配置类似的消息转换器，将order映射为它自己能够理解的订单类型。在接收端的订单可能位于不同的包中、有不同的类名，甚至可以只包含发送者Order属性的一个子集。
     *
     * @return
     */
    @Bean("messageConverter2")
    public MappingJackson2MessageConverter messageConverter2() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");
        // 为了实现更大的灵活性，我们可以通过调用消息转换器的setTypeIdMappings()方法将一个合成类型名映射到实际类型上。举例来说，消息转换器bean方法的如下代码变更会将一个合成的order类型ID映射为Order类：
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("order", Order.class);
        messageConverter.setTypeIdMappings(typeIdMappings);
        return messageConverter;
    }
}
