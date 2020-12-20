package com.djl.tacocloud.config;

import com.djl.tacocloud.service.UserRepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

/**
 * @author djl
 * @create 2020/12/20 9:12
 */
@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Qualifier("userRepositoryUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }

    /**
     * 这些规则的顺序是很重要的。声明在前面的安全规则比后面声明的规则有更高的优先级。如果我们交换这两个安全规则的顺序，那么所有的请求都会有permitAll()的规则，对“/design”和“/orders”声明的规则就不会生效了。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // •具备ROLE_USER权限的用户才能访问“/design”和“/orders”；
                .antMatchers("/design", "/orders")
                .hasRole("USER")
                // •其他的请求允许所有用户访问。
                .antMatchers("/", "/**").permitAll()
                .and()
                //默认情况下，Spring Security会在“/login”路径监听登录请求并且预期的用户名和密码输入域的名称为username和password
                .formLogin().loginPage("/login")
                // 另外，我们还可以强制要求用户在登录成功之后统一访问设计页面，即便用户在登录之前正在访问其他页面，在登录之后也会被定向到设计页面，这可以通过为defaultSuccessUrl方法传递第二个参数true来实现：
                .defaultSuccessUrl("/design", true)
                .and()
                .logout().logoutSuccessUrl("/");//.loginProcessingUrl("/authenticate");
        // Spring Security 默认开启了 CSRF 的保护，H2Database 相关的请求需要携带 CSRF Token 及相关参数，所以访问时候出现了 403 。
        http.csrf().ignoringAntMatchers("/h2-console/**");
        // Spring Security 默认页面不允许 iframe （不安全），会在响应头返回：X-Frame-Options:DENY
        http.headers().frameOptions().sameOrigin(); // 允许同源使用 iframe
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("djl")
//                .password("111111")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("test")
//                .password("123456")
//                .authorities("ROLE_USER");

        // 使用自定义实现作为权限框架的逻辑
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder()); // 看上去，我们调用了encoder()方法，并将返回值传递给passwordEncoder()。实际上，encoder()方法带有@Bean注解，它将用来在Spring应用上下文中声明PasswordEncoder bean。对于encoder()的任何调用都会被拦截，并且会返回应用上下文中的bean实例。

//        auth.jdbcAuthentication().dataSource(dataSource);

        // 在本例中，我们只重写了认证和基本权限的查询语句，但是通过调用groupAuthoritiesByUsername()方法，我们也能够将群组权限重写为自定义的查询语句。将默认的SQL查询替换为自定义的设计时，很重要的一点就是要遵循查询的基本协议。所有查询都将用户名作为唯一的参数。认证查询会选取用户名、密码以及启用状态信息。权限查询会选取零行或多行包含该用户名及其权限信息的数据。群组权限查询会选取零行或多行数据，每行数据中都会包含群组ID、群组名称以及权限。
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username,password,enable from Users" +
//                        "where username=?")
//                .authoritiesByUsernameQuery("select username,authority from UserAuthorities" +
//                        "where username=?")
//                .passwordEncoder(new StandardPasswordEncoder("53cr3t")); // •BCryptPasswordEncoder：使用bcrypt强哈希加密。•NoOpPasswordEncoder：不进行任何转码。•Pbkdf2PasswordEncoder：使用PBKDF2加密。•SCryptPasswordEncoder：使用scrypt哈希加密。•StandardPasswordEncoder：使用SHA-256哈希加密。

        // 为了配置Spring Security使用基于LDAP认证，我们可以使用ldapAuthentication()方法
//        auth.ldapAuthentication()
//                .userSearchBase("ou=people")
//                .userSearchFilter("(uid={0})")
//                .groupSearchBase("ou=groups")
//                .groupSearchFilter("member={0}")
//                .passwordCompare()
//                .passwordEncoder(new BCryptPasswordEncoder()) // 密码使用bcrypt密码哈希函数加密。这需要LDAP服务器上的密码也使用bcrypt进行了加密。
//                .passwordAttribute("passcode") // 我们指定了要与给定密码进行比对的是“passcode”属性
//                .and().contextSource()
//                .url("ldap://tacocloud.com:33389/dc=tacocloud,dc=com") // 默认情况下，Spring Security的LDAP认证假设LDAP服务器监听本机的33389端口。但是，如果你的LDAP服务器在另一台机器上，那么可以使用contextSource()方法来配置这个地址：
//                .root("dc=tacocloud,dc=com") // 如果你没有现成的LDAP服务器供认证使用，Spring Security还为我们提供了嵌入式的LDAP服务器
//                .ldif("classpath:users.ldif"); // 在这里，我们明确要求LDAP服务器从类路径根目录下的users.ldif文件中加载内容。

    }
}
