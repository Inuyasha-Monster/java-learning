package com.djl.configclienterueka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author djl
 * @create 2020/12/22 14:59
 */
@RestController
@RequestMapping("/api")
@RefreshScope
public class GitController {
    @Autowired
    private GitConfig gitConfig;

    @Autowired
    private GitAutoRefreshConfig gitAutoRefreshConfig;

    @GetMapping(value = "/show")
    public Object show() {
        return gitConfig;
    }

    @GetMapping(value = "/autoShow")
    public Object autoShow() {
        return gitAutoRefreshConfig;
    }
}
