package com.djl.tacocloud.entity;

import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNullFields;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
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
@Entity
@RestResource(rel = "tacos", path = "tacos") // @RestResource注解能够为实体提供任何我们想要的关系名和路径。在本例中，我们将它们都设置成了“tacos”
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 5, message = "最小5个字符")
    private String name;

    private Date createdAt;

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "至少选择一个")
    private List<Ingredient> ingredients;

    /**
     * 在Taco持久化之前，我们会使用这个方法将createdAt设置为当前的日期和时间
     */
    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }


//
//    public List<Ingredient> getIngredients() {
//        if (ingredients != null && ingredients.size() > 0) {
//            List<Ingredient> list = ingredients.stream().map(x -> new Ingredient(x, null, null)).collect(Collectors.toList());
//            return list;
//        }
//        return new ArrayList<>();
//    }
}
