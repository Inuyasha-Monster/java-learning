package com.djl.apiconsumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author djl
 * @create 2020/12/22 11:08
 * 接口上的@FeignClient注解会指定该接口上的所有方法都会对名为ingredient-service的服务发送请求。在内部，服务将会通过Ribbon进行查找，这与支持负载均衡的RestTemplate运行方式是一样的。
 */
@FeignClient(name = "api-service")
public interface ApiClientService {
    @GetMapping("/api/getList")
    Iterable<String> getList();
}
