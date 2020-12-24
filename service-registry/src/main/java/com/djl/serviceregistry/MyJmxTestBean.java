package com.djl.serviceregistry;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author djl
 * @create 2020/12/24 14:32
 * 使用了 @Component 注解，所以它将会被组件扫描功能所发现并且会注册一个实例作为bean存放到Spring应用上下文中。它还使用了@ManagedResource注解，表明这个bean是一个MBean
 */
@Component
@ManagedResource
public class MyJmxTestBean implements NotificationPublisherAware {
    private AtomicInteger atomicInteger = new AtomicInteger();
    private NotificationPublisher notificationPublisher;

    /**
     * @return
     * @ManagedAttribute注解，将会暴露为一个MBean属性
     */
    @ManagedAttribute
    public int getAtomicInteger() {
        return atomicInteger.get();
    }

    /**
     * increment()方法使用了@ManagedOperation注解，将会暴露为MBean操作。
     *
     * @return
     */
    @ManagedOperation
    public int incrementAtomic() {
        if (atomicInteger.incrementAndGet() % 2 == 0) {
            notificationPublisher.sendNotification(new Notification("odd.count", this, atomicInteger.get()));
        }
        return atomicInteger.get();
    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
