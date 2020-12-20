package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author djl
 * @create 2020/12/18 17:21
 */
public interface OrderRepository {

    Order save(Order order);

    List<Order> findByUserOrderByPlacedAtDesc(User user);
}
