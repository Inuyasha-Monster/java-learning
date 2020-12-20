package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.User;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author djl
 * @create 2020/12/18 17:21
 */
public interface OrderRepository {

    Order save(Order order);

    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
