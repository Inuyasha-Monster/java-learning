package com.djl.apiregistry;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @author djl
 * @create 2020/12/21 22:12
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Value("${server.port}")
    String port;

    @GetMapping("/timeout")
    @HystrixCommand(fallbackMethod = "timeoutFallback")
    public String timeout() {
        return "ok";
    }

    private String timeoutFallback() {
        return "timeout";
    }

    @GetMapping("/getList")
    public Iterable<String> getList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        arrayList.add(port);
        return arrayList;
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getRemote")
    public Iterable<String> getRemote() {
        Iterable<String> result = restTemplate.getForObject("http://api-service/api/getList", Iterable.class);
        return result;
    }
}
