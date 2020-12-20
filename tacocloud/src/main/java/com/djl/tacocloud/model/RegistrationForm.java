package com.djl.tacocloud.model;

import com.djl.tacocloud.entity.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author djl
 * @create 2020/12/20 10:11
 */
@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), fullname, street, city, state, zip, phone);
    }
}
