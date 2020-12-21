package com.djl.tacocloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author djl
 * @create 2020/12/21 16:42
 */
@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {
    /**
     * 最重要的是，配置是通过给定的ServerHttpSecurity对象进行声明的，而不是通过HttpSecurity对象。借助ServerHttpSecurity，我们可以调用authorizeExchange()，它大致等价于authorizeRequests()，都是用来声明请求级的安全性的。
     *
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // ServerHttpSecurity是Spring Security 5新引入的，在反应式编程中它模拟了HttpSecurity的功能。
        return http.authorizeExchange().pathMatchers("/design", "/orders").hasAuthority("USER")
                .anyExchange().permitAll()
                .and().build();
    }
}
