package com.djl.tacocloud.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author djl
 * @create 2020/12/24 9:51
 * 自定义健康检查机制
 * 如果是下午，那么所返回的健康状态是OUT_OF_SERVICE，其中还包含导致该状态的原因详情。即便是在午饭前，这个健康指示器也有10%的概率报告DOWN状态，因为它使用随机数来决定应用是否正常启动。如果随机数的值小于0.1，那么状态将是DOWN，否则状态将是UP。
 */
@Component
public class MyHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour > 12) {
            return Health.outOfService()
                    .withDetail("reason", "i'm out of service after lunchtime").build();
        }
        if (Math.random() < 0.1) {
            return Health.down().withDetail("reason", "i break 10% of the time").build();
        }
        return Health.up().withDetail("reason", "all is good").build();
    }
}
