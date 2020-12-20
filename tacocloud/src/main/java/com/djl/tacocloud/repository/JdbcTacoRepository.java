package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.Ingredient;
import com.djl.tacocloud.entity.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Optionals;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author djl
 * @create 2020/12/18 17:23
 */
@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        final long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }
        return null;
    }

    @Override
    public List<Taco> findAll(PageRequest pageRequest) {
        List<Taco> tacos = jdbc.queryForList("select * from Taco", Taco.class);
        return tacos;
    }

    @Override
    public Optional<Taco> findById(long id) {
        Taco taco = jdbc.queryForObject("select * from Taco where id=?", Taco.class, id);
        Optional<Taco> result = Optional.ofNullable(taco);
        return result;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreator pst = new PreparedStatementCreatorFactory("insert into Taco(name,createdAt) values(?,?)",
                Types.VARCHAR, Types.TIMESTAMP)
                .newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(pst, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbc.update("insert into TACO_INGREDIENTS(taco,ingredient)" +
                        "values (?,?)",
                tacoId, ingredient.getId());
    }
}
