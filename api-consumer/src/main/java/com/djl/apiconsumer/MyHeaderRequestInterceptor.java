package com.djl.apiconsumer;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author djl
 * @create 2020/12/30 14:51
 */
@Configuration
public class MyHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("fuck", "me");
    }
}
