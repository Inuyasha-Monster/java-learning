package com.djl.apiregistry;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    @HystrixCommand(fallbackMethod = "timeoutFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    })
    public String timeout() {
        Random random = new Random(System.currentTimeMillis());
        final int timeout = random.nextInt(1000);
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    private String timeoutFallback() {
        return "timeout";
    }

    @GetMapping("/getList")
    @ResponseBody
    public Iterable<String> getList(HttpServletRequest httpRequest) {

        // 判断请求头是否符合要求
        final String fuck = httpRequest.getHeader("fuck");
        if (StringUtils.isEmpty(fuck)) {
            return new ArrayList<>();
        }

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
