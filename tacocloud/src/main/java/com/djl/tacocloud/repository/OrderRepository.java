package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;

/**
 * @author djl
 * @create 2020/12/18 17:21
 */
public interface OrderRepository {
    Order save(Order order);
}
