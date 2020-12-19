package com.djl.tacocloud.entity;

import lombok.Data;
import org.springframework.lang.NonNullFields;
import org.springframework.web.bind.annotation.Mapping;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author djl
 * @create 2020/12/18 11:39
 */
@Data
public class Taco {

    private long id;
    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "最小5个字符")
    private String name;

    @Size(min = 1, message = "至少选择一个")
    private List<String> ingredients;

    public List<Ingredient> getIngredients() {
        if (ingredients != null && ingredients.size() > 0) {
            List<Ingredient> list = ingredients.stream().map(x -> new Ingredient(x, null, null)).collect(Collectors.toList());
            return list;
        }
        return new ArrayList<>();
    }
}
