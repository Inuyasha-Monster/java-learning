package com.djl.apiconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author djl
 * @create 2020/12/22 10:42
 */

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    RestTemplate restTemplate;

    /**
     * 从eureka中获取远程服务地址然后采取默认的轮询的方式调用远程服务
     * @return
     */
    @GetMapping("/getRemote")
    public Iterable<String> getRemote() {
        final Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", java.lang.Iterable.class);
        return result;
    }
}
