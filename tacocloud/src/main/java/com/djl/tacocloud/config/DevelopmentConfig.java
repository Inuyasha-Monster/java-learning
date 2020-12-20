package com.djl.tacocloud.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author djl
 * @create 2020/12/20 15:14
 * 开发环境的配置
 * 在这里，CommandLineRunner bean（包括DevelopmentConfig中定义的其他bean）只有在prod和qa均没有激活的情况下才会创建。
 */
@Profile({"!prod", "!qa"})
@Configuration
public class DevelopmentConfig {
    @Bean
    public CommandLineRunner dataLoader() {
        return null;
    }
}
