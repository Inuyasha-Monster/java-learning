package com.djl.apiconsumer.controller;

import com.djl.apiconsumer.feign.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author djl
 * @create 2020/12/22 10:42
 */

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ApiClientService apiClientService;

    /**
     * 从eureka中获取远程服务地址然后采取默认的轮询的方式调用远程服务
     *
     * @return
     */
    @GetMapping("/getRemote")
    public Iterable<String> getRemote() {
        final Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", java.lang.Iterable.class);
        return result;
    }

    @GetMapping("/getRemote2")
    public Mono<Iterable<String>> getRemote2() {
        final Mono<Iterable> result = webClientBuilder.build().get().uri("http://api-service/api/getList")
                .retrieve().bodyToMono(Iterable.class);
        final Mono<Iterable<String>> map = result.map(x -> ((Iterable<String>) x));
        return map;
    }

    @GetMapping("/getRemote3")
    public Iterable<String> getRemote3() {
        final Iterable<String> list = apiClientService.getList();
        return list;
    }
}
