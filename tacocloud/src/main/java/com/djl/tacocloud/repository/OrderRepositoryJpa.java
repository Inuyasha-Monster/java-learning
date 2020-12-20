package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author djl
 * @create 2020/12/20 8:55
 * findByDeliveryZip ()方法非常简单，但是Spring Data也能处理更加有意思的方法名称。repository方法是由一个动词、一个可选的主题（Subject）、关键词By以及一个断言所组成的。在findByDeliveryZip()这个样例中，动词是find，断言是DeliveryZip，主题并没有指定，暗含的主题是Order。
 */
public interface OrderRepositoryJpa extends CrudRepository<Order, Long> {

    List<Order> findByZip(String zip);

    List<Order> findByCityOrderByCcNumber(String city);

    /**
     * 通过使用@Query，我们声明只查询所有投递到Seattle的订单。但是，我们可以使用@Query执行任何想要的查询，有些查询是通过方法命名约定很难甚至根本无法实现的。
     *
     * @return
     */
//    @Query("Order o where o.city='seattle'")
//    List<Order> readOrdersByDeliveredInSeattle();
}
