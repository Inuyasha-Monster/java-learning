package com.djl.tacocloud.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author djl
 * @create 2020/12/18 11:22
 * NoArgsConstructor 注意：
 * 当类中有 final 字段没有被初始化时，编译器会报错，此时可用 @NoArgsConstructor(force = true)，然后就会为没有初始化的 final 字段设置默认值 0 / false / null。对于具有约束的字段（例如 @NonNull 字段），不会生成检查或分配，因此请注意，正确初始化这些字段之前，这些约束无效。
 * 为了将Ingredient声明为JPA实体，它必须添加@Entity注解。它的id属性需要使用@Id注解，以便于将其指定为数据库中唯一标识该实体的属性。
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true) // 注解在类上，为类提供一个无参的构造方法。
@Entity
public class Ingredient {
    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
