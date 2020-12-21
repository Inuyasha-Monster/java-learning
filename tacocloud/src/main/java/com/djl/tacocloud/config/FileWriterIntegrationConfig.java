package com.djl.tacocloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.file.dsl.Files;

import java.io.File;

/**
 * @author djl
 * @create 2020/12/21 10:35
 */
@Configuration
public class FileWriterIntegrationConfig {

    /**
     * 转换器bean使用了@Transformer注解，这样会将其声明成集成流中的一个转换器，它接受来自textInChannel通道的消息，然后将消息写入到名为fileWriterChannel的通道中。GenericTransformer是一个函数式接口，所以我们可以使用lambda表达式为其提供实现，这里调用了消息文本的toUpperCase()方法
     *
     * @return
     */
    @Bean
    @Transformer(inputChannel = "textInChannel", outputChannel = "fileWriterChannel")
    public GenericTransformer<String, String> upperCaseTransformer() {
        return x -> x.toUpperCase();
    }

    /**
     * @return
     * @ServiceActivator注解，表明它会接受来自fileWriter Channel的消息，并且会将消息传递给FileWritingMessageHandler实例所定义的服务
     * FileWritingMessageHandler是一个消息处理器，它会将消息的载荷写入特定目录的一个文件中，而文件的名称是通过消息的file_name头信息指定的
     */
    @Bean
    @ServiceActivator(inputChannel = "fileWriterChannel")
    public FileWritingMessageHandler fileWriter() {
        final FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File("/tmp/sia5/files"));
        // setExpectReply(false)方法，通过这个方法能够告知服务激活器（service activator）不要期望会有答复通道（reply channel，通过这样的通道，我们可以将某个值返回到流中的上游组件）
        fileWritingMessageHandler.setExpectReply(false);
        fileWritingMessageHandler.setFileExistsMode(FileExistsMode.APPEND);
        fileWritingMessageHandler.setAppendNewLine(true);
        return fileWritingMessageHandler;
    }

    @Bean
    public MessageChannel textChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileWriterChannel() {
        return new DirectChannel();
    }

    /**
     * 使用Spring Integration的DSL配置
     */
    @Bean
    public IntegrationFlow fileWriterFlow() {
        return IntegrationFlows.from(MessageChannels.direct("textChannel"))
                .<String, String>transform(x -> x.toUpperCase())
                .handle(Files.outboundAdapter(new File("/tmp/sia5/files"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .appendNewLine(true)).get();
    }
}
