package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

/**
 * @author djl
 * @create 2020/12/20 8:53
 */
public interface IngredientRepositoryJpa extends CrudRepository<Ingredient, String> {
}
