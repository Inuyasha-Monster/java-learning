package com.djl.tacocloud.integration;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.router.AbstractMappingMessageRouter;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.integration.router.MessageRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author djl
 * @create 2020/12/21 13:59
 */
@Configuration
public class IntegrationConfig {
    /**
     * PublishSubscribeChannel：发送到PublishSubscribeChannel的消息会被传递到一个或多个消费者中。如果有多个消费者，它们都会接收到消息。
     *
     * @return
     */
    @Bean
    public MessageChannel orderChannel() {
        return new PublishSubscribeChannel();
    }

    /**
     * 如果使用QueueChannel，消费者必须配置一个poller
     *
     * @return
     */
    @Bean(name = "orderChannel2")
    public MessageChannel orderChannel2() {
        return new QueueChannel();
    }

    /**
     * 服务激活器每秒（或者说每1000毫秒）都会轮询名为 orderChannel2 的通道。
     *
     * @return
     */
    @ServiceActivator(inputChannel = "orderChannel2", poller = @Poller(fixedRate = "1000"))
    public MessageHandler orderChannel2Handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                return;
            }
        };
    }

    /**
     * 例如，假设消息包含了整型的值，它们要通过名为numberChannel的通道进行发布，但是我们只想让偶数进入名为evenNumberChannel的通道。在这种情况下，我们可以使用@Filter注解定义一个过滤器：
     *
     * @param number
     * @return
     */
    @Filter(inputChannel = "numberChannel", outputChannel = "evenNumberChannel")
    public boolean evenNumberFilter(Integer number) {
        return number % 2 == 0;
    }

    @Bean
    public IntegrationFlow eventNumberFlow() {
        return IntegrationFlows.from(MessageChannels.direct("numberChannel"))
                .<Integer>filter(x -> x % 2 == 0)
                .<Integer, String>transform(x -> x.toString())
                .handle(Files.outboundAdapter(new File("/tmp/sia5/files"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true)).get();
    }

    @Bean
    @Transformer(inputChannel = "numberChannel", outputChannel = "stringChannel")
    public GenericTransformer<Integer, String> romanNumberTransformer() {
        return x -> x.toString();
    }

    @Bean
    @Router(inputChannel = "numberChannel")
    public AbstractMessageRouter evenOddRouter() {
        return new AbstractMappingMessageRouter() {
            @Override
            protected List<Object> getChannelKeys(Message<?> message) {
                final Integer payload = (Integer) message.getPayload();
                if (payload % 2 == 0) {
                    return Collections.singletonList(evenChannel());
                }
                return Collections.singletonList(oddChannel());
            }
        };
    }

    @Bean
    public MessageChannel evenChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel oddChannel() {
        return new DirectChannel();
    }

    /**
     * 根据数字通道中的消息取模运算之后路由到不同的channel进而不同的handle处理消息
     *
     * @return
     */
    @Bean
    public IntegrationFlow numberRoutingFlow() {
        return IntegrationFlows.from(MessageChannels.direct("numberChannel"))
                .<Integer, String>route(x -> x % 2 == 0 ? "even" : "odd", map -> map.subFlowMapping("even", sf -> sf.<Integer, Integer>transform(n -> n * 10).handle(Files.outboundAdapter(new File("/tmp/sia5/files"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true)))
                        .subFlowMapping("odd", sf -> sf.transform(romanNumberTransformer()).handle(Files.outboundAdapter(new File("/tmp/sia6/files"))
                                .fileExistsMode(FileExistsMode.APPEND)
                                .appendNewLine(true))))
                .get();
    }


    /**
     * 切割器:例如，假设希望将携带购买订单的消息拆分为两条消息：一条携带账单信息，另一条携带项目列表
     *
     * @return
     */
    @Bean
    @Splitter(inputChannel = "poChannel", outputChannel = "splitOrderChannel")
    public OrderSplitter orderSplitter() {
        return new OrderSplitter();
    }

    /**
     * 在这里，购买订单到达名为 poChannel 的通道，并被 OrderSplitter 分割。然后，将返回集合中的每个项作为集成流中的单独消息发布到名为 splitOrderChannel 的通道。在流的这一点上，可以声明一个 PayloadTypeRouter 来将账单信息和项目，并路由到它们自己的子流 PayloadTypeRouter 其实就路由器
     *
     * @return
     */
    @Bean
    @Router(inputChannel = "splitOrderChannel")
    public MessageRouter splitOrderRouter() {
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(
                BillingInfo.class.getName(), "billingInfoChannel");
        router.setChannelMapping(List.class.getName(), "lineItemsChannel");
        return router;
    }

    @Splitter(inputChannel = "lineItemsChannel", outputChannel = "lineItemChannel")
    public List<LineItem> lineItemSplitter(List<LineItem> lineItems) {
        return lineItems;
    }

// 当携带 List<LineItem> 的有效负载的消息到达名为 lineItemsChannel 的通道时，它将传递到 lineItemSplitter() 方法。根据 Splitter 的规则，该方法必须返回要 Splitter 的项的集合。在本例中，已经有了 LineItems 的集合，因此只需直接返回该集合。因此，集合中的每个 LineItem 都以其自己的消息形式发布到名为 lineItemChannel 的通道。
//如果你想使用 Java DSL 来声明相同的 Splitter/Router 配置，你可以调用 split() 和 route()：
//    return IntegrationFlows
//    ...
//            .split(orderSplitter())
//            .<Object, String> route(p -> {
//        if (p.getClass().isAssignableFrom(BillingInfo.class)) {
//            return "BILLING_INFO";
//        } else {
//            return "LINE_ITEMS";
//        }
//    }, mapping ->
//            mapping.subFlowMapping("BILLING_INFO", sf ->
//    sf.<BillingInfo> handle((billingInfo, h) -> { ... }))
//            .subFlowMapping("LINE_ITEMS", sf ->
//            sf.split().<LineItem> handle((lineItem, h) -> { ... }))
//            )
//            .get();

    /**
     * 通过 @ServiceActivator 注解 bean，将其指定为一个服务激活器，从所述信道处理消息命名 someChannel。至于 MessageHandler 的本身，它是通过一个 lambda 实现。虽然这是一个简单的 MessageHandler，给定的消息时，它发出其有效载荷的标准输出流。 MessageHandler 是没有返回值的
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "someChannel")
    public MessageHandler sysoutHandler() {
        return message -> {
            System.out.println("Message payload: " + message.getPayload());
        };
    }

    /**
     * 可以声明一个服务激活器，用于在返回一个新的有效载荷之前处理传入的消息。在这种情况下，这个 bean 应该是一个 GenericHandler 而非的 MessageHandler：
     * 在这种情况下，服务激活器是一个 GenericHandler，其中的有效载荷为 Order 类型。当订单到达，它是通过 repository 进行保存；保存 Order 后产生的结果被发送到名称为 completeChannel 的输出通道。注意，GenericHandler 不仅给出了有效载荷，还有消息头（即使该示例不使用任何形式的头信息）具有返回值的可能性
     * @param orderRepo
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "orderChannel", outputChannel = "completeOrder")
    public GenericHandler<Order> orderHandler(OrderRepository orderRepo) {
        return (payload, headers) -> {
            return orderRepo.save(payload);
        };
    }

}
