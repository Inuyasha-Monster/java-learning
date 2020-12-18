package com.djl.tacocloud.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author djl
 * @create 2020/12/18 11:22
 */
@Data
@RequiredArgsConstructor
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
