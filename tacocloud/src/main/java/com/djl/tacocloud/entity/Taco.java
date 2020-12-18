package com.djl.tacocloud.entity;

import lombok.Data;
import org.springframework.lang.NonNullFields;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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
    private List<Ingredient> ingredients;
}
