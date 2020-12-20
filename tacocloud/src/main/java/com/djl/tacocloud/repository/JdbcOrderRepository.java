package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.Taco;
import com.djl.tacocloud.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author djl
 * @create 2020/12/18 17:55
 */
@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final SimpleJdbcInsert orderInsert;
    private final SimpleJdbcInsert orderTacoInsert;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        // 第一个实例赋值给了orderInserter实例变量，配置为与Taco_Order表协作，并且假定id属性将会由数据库提供或生成
        this.orderInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("Taco_Order").usingGeneratedKeyColumns("id");
        // 第二个实例赋值给了orderTacoInserter实例变量，配置为与Taco_Order_Tacos表协作，但是没有声明该表中ID是如何生成的。
        this.orderTacoInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("Taco_Order_Tacos");
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    private long saveOrderDetails(Order order) {
        final Map values = objectMapper.convertValue(order, Map.class);
        // Map创建完成之后，我们将Map中placedAt条目的值设置为Order对象placedAt属性的值。之所以需要这样做，是因为ObjectMapper会将Date属性转换为long，这会导致与Taco_Order表中的placedAt字段不兼容。
        values.put("placedAt", order.getPlacedAt());
        final long orderId = orderInsert.executeAndReturnKey(values).longValue();
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInsert.execute(values);
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        final long orderId = saveOrderDetails(order);
        order.setId(orderId);
        final List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }
        return order;
    }

    @Override
    public List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable) {
        List<Order> orders = jdbcTemplate.queryForList("select * from Taco_Order a where a.USER_ID=? order by a.PLACED_AT desc", Order.class, user.getId());
        return orders;
    }

}
