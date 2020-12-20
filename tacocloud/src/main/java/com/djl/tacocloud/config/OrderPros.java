package com.djl.tacocloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author djl
 * @create 2020/12/20 15:01
 * 尽管我们很容易就可以将@Validated、@Min和@Max注解用到OrderController（和其他可以注入OrderProps的地方），但是这样会使OrderController更加混乱。通过配置属性的持有者bean，我们将所有的配置属性收集到了一个地方，这样就能让使用这些属性的bean尽可能保持整洁。
 */
@Component
@ConfigurationProperties(prefix = "taco.orders")
@Data
@Validated
public class OrderPros {
    @Min(value = 5)
    @Max(value = 25)
    private int pageSize = 20;
}
