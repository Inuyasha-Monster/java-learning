package com.djl.tacocloud.config;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author djl
 * @create 2020/12/18 16:30
 * 视图控制器：也就是只将请求转发到视图而不做其他事情的控制器。
 */
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // addViewControllers()方法会接收一个ViewControllerRegistry对象，我们可以使用它注册一个或多个视图控制器。在这里，我们调用registry的addViewController()方法，将“/”传递了进去，视图控制器将会针对该路径执行GET请求。这个方法会返回ViewControllerRegistration对象，我们马上基于该对象调用了setViewName()方法，用它指明当请求“/”的时候要转发到“home”视图上。
        registry.addViewController("/").setViewName("home");

        // 妈的这里配置为何不起作用
        registry.addViewController("/login").setViewName("login");
    }
}
