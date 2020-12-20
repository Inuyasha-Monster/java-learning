package com.djl.tacocloud.repository;

import com.djl.tacocloud.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author djl
 * @create 2020/12/20 9:35
 * Spring Data JPA会在运行时自动生成这个接口的实现
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);
}
