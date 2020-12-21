package com.djl.apiregistry;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getList")
    public Iterable<String> getList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
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
