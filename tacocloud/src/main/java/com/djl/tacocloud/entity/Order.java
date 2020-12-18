package com.djl.tacocloud.entity;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author djl
 * @create 2020/12/18 15:21
 */
@Data
public class Order {
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

    @CreditCardNumber
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0)
    private String ccCVV;
}
