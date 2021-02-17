package com.djl.apiconsumer.controller;

import com.djl.apiconsumer.feign.ApiClientService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("/getRemote4")
    @HystrixCommand(fallbackMethod = "getDefault")
    public Iterable<String> getRemote4() {
        final Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", java.lang.Iterable.class);
        return result;
    }

    private Iterable<String> getDefault() {
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("default");
        return strings;
    }

    @GetMapping("/getRemote5")
    @HystrixCommand(fallbackMethod = "getDefault1", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    })
    public Iterable<String> getRemote5() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        final Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", java.lang.Iterable.class);
        return result;
    }

    private Iterable<String> getDefault1() {
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("timeout");
        return strings;
    }

    private Iterable<String> getDefault2() {
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("fallback");
        return strings;
    }

    /**
     * •circuitBreaker.requestVolumeThreshold：在给定的时间范围内，方法被调用的次数。
     * •circuitBreaker.errorThresholdPercentage：在给定的时间范围内，方法调用产生失败的百分比。
     * •metrics.rollingStats.timeInMilliseconds：控制请求量和错误百分比的滚动时间周期。
     * •circuitBreaker.sleepWindowInMilliseconds：处于打开状态的断路器要经过多长时间才会进入半开状态，进入半开状态之后，将会再次尝试失败的原始方法。
     * 调整失败的设置：将其变更为在20秒的时间范围内调用超过30次且失败率超过25%,打开状态之后断路器必须保持1分钟，然后才进入半开状态
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/getRemote6")
    @HystrixCommand(
            fallbackMethod = "getDefault2",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "30"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "25"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "20000"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000")
            })
    public Iterable<String> getRemote6() throws InterruptedException {
        final Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", java.lang.Iterable.class);
        return result;
    }

}
