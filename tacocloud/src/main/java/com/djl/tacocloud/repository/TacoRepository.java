package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.Taco;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author djl
 * @create 2020/12/18 17:21
 */
public interface TacoRepository {
    Taco save(Taco design);

    List<Taco> findAll(PageRequest pageRequest);

    Optional<Taco> findById(long id);
}
