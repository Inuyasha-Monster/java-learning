package com.djl.tacocloud.integration;

import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author djl
 * @create 2020/12/21 10:28
 * 它使用了@MessagingGateway注解。这个注解会告诉Spring Integration要在运行时生成该接口的实现
 * @MessagingGateway的defaultRequestChannel属性表明接口方法调用时所返回的消息要发送至给定的消息通道（message channel）。在本例中，我们声明调用writeToFile()所形成的消息应该发送至名为textInChannel的通道中。
 * @Header注解表明传递给filename的值应该包含在消息头信息中（通过FileHeaders.FILENAME声明，它将会被解析成file_name），而不是放到消息载荷（payload）中
 */

/**
 * 当FileWriterGateway的writeToFile()方法被调用的时候，结果形成的消息将会发布到这个通道上。
 */
@MessagingGateway(defaultRequestChannel = "textInChannel")
@ImportResource("classpath:/filewriter-config.xml") // 如果想要在Spring Boot应用中使用XML配置，那么我们需要将XML作为资源导入到Spring应用中。最简单的实现方式就是在应用的某个Java配置类上使用Spring的@ImportResource注解
public interface FileGeteway {
    void WriteToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
