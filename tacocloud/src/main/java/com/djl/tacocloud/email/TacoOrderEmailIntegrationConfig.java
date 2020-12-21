package com.djl.tacocloud.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

/**
 * @author djl
 * @create 2020/12/21 15:08
 * taco 订单电子邮件流（在 tacoOrderEmailFlow() 方法中的定义）是由三个不同的部分组成：
 * IMAP 电子邮件入站信道适配器 —— 根据 EmailProperties 的 getImapUrl() 方法返回的 IMP URL 来创建通道适配器，根据 pollRate属性来设定轮询延时。进来的电子邮件被移交到它连接到转换器的通道。
 * 一种将电子邮件转换为订单对象的转换器 —— 在 EmailToOrderTransformer 中实现的转换器，其被注入到 tacoOrderEmailFlow() 方法中。从转换中所产生的订单通过另外一个通道扇出到最终组件中。
 * *处理程序（作为出站通道适配器）—— 处理程序接收一个订单对象，并将其提交到 Taco Cloud 的 REST API。
 */
@Configuration
public class TacoOrderEmailIntegrationConfig {

    @Bean
    public IntegrationFlow tacoOrderEmailFlow(
            EmailProperties emailProps,
            EmailToOrderTransformer emailToOrderTransformer,
            OrderSubmitMessageHandler orderSubmitHandler) {

        return IntegrationFlows
                .from(Mail.imapInboundAdapter(emailProps.getImapUrl()),
                        e -> e.poller(
                                Pollers.fixedDelay(emailProps.getPollRate())))
                .transform(emailToOrderTransformer)
                .handle(orderSubmitHandler)
                .get();
    }
}
