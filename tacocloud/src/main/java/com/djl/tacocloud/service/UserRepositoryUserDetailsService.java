package com.djl.tacocloud.service;

import com.djl.tacocloud.entity.User;
import com.djl.tacocloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author djl
 * @create 2020/12/20 9:38
 */
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User '" + s + "' not fount");
    }
}
