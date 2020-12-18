package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Ingredient;

/**
 * @author djl
 * @create 2020/12/18 16:47
 */
public interface IngredientRepository {
    Iterable<Ingredient> findAll();

    Ingredient findOne(String id);

    Ingredient save(Ingredient ingredient);
}
