package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Taco;
import org.springframework.data.repository.CrudRepository;

/**
 * @author djl
 * @create 2020/12/20 8:54
 */
public interface TacoRepositoryJpa extends CrudRepository<Taco, Long> {
}
