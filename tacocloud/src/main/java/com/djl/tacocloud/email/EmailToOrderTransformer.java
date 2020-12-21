package com.djl.tacocloud.email;

import com.djl.tacocloud.entity.Order;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author djl
 * @create 2020/12/21 15:08
 */
public class EmailToOrderTransformer {//extends AbstractMailMessageTransformer<Order> {

//    @Override
//    protected AbstractIntegrationMessageBuilder<Order> doTransform(javax.mail.Message mailMessage) throws Exception {
////        Order tacoOrder = processPayload(mailMessage);
//        return null;//MessageBuilder.withPayload(tacoOrder);
//    }
//
//    private Order processPayload(Message mailMessage) {
//        return null;
//    }
}
