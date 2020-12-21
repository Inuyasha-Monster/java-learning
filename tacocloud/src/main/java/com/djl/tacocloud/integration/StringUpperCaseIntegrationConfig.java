package com.djl.tacocloud.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

/**
 * @author djl
 * @create 2020/12/21 14:43
 */
@Configuration
public class StringUpperCaseIntegrationConfig {

    /**
     * 正如这里所定义的，流程启动于名为 inChannel 的通道获得数据输入的时候。然后消息的有效负载通过转换器去执行变成大写字母的操作，这里的操作都使用 lambda 表达式进行定义。消息的处理结果被发布到名为 outChannel 的通道中，这个通道就是已经被声明为 UpperCaseGateway 接口的答复通道
     *
     * @return
     */
    @Bean
    public IntegrationFlow uppercaseFlow() {
        return IntegrationFlows
                .from("inChannel")
                .<String, String>transform(s -> s.toUpperCase())
                .channel("outChannel")
                .get();
    }
}
