package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Taco;

/**
 * @author djl
 * @create 2020/12/18 17:21
 */
public interface TacoRepository {
    Taco save(Taco design);
}
