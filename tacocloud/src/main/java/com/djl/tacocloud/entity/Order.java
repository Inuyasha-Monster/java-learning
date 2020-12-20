package com.djl.tacocloud.entity;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author djl
 * @create 2020/12/18 15:21
 * Taco_Order
 */
@Data
@Entity
@Table(name = "Taco_Order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 多个订单对应一个用户
     */
    @ManyToOne
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date placedAt;

    @NotBlank
    private String name;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zip;

    //    @CreditCardNumber
    @NotNull
    private String ccNumber;

    //    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$")
    @NotNull
    private String ccExpiration;

    //    @Digits(integer = 3, fraction = 0)
    @NotNull
    private String ccCVV;

    @ManyToMany(targetEntity = Taco.class)
    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco taco) {
        this.tacos.add(taco);
    }

    @PrePersist
    void createdAt() {
        this.placedAt = new Date();
    }
}
