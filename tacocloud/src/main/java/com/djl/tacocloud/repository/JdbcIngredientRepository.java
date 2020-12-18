package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author djl
 * @create 2020/12/18 16:49
 * 为JdbcIngredientRepository添加@Repository注解之后，Spring的组件扫描就会自动发现它，并且会将其初始化为Spring应用上下文中的bean。
 */
@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        final List<Ingredient> ingredients = jdbcTemplate.query("select id, name, type from Ingredient", this::mapRowToIngredient);
        return ingredients;
    }

    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(resultSet.getString("id"), resultSet.getString("name"), Ingredient.Type.valueOf(resultSet.getString("type")));
    }

    @Override
    public Ingredient findOne(String id) {
        return jdbcTemplate.queryForObject("select id, name, type from Ingredient where id=?", this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update("insert into Ingredient(id,name,type) values(?,?,?)", ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
        return ingredient;
    }
}
