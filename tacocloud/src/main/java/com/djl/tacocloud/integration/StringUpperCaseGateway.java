package com.djl.tacocloud.integration;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

/**
 * @author djl
 * @create 2020/12/21 14:40
 * 作为一个例子，假设一个网关处理接受一个 String 的简单集成信息流，并把特定的 String 转成大写。网关接口可能是这个样子：
 */
@Component
@MessagingGateway(defaultRequestChannel = "inChannel", defaultReplyChannel = "outChannel")
public interface StringUpperCaseGateway {
    String uppercase(String in);
}
