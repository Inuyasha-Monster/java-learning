package com.djl.tacocloud.service;

import com.djl.tacocloud.entity.Order;

/**
 * @author djl
 * @create 2020/12/20 16:33
 */
public interface OrderMessagingService {
    void sendOrder(Order order);

    Order receiveOrder();
}
