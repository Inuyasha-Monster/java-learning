package com.djl.springboot.config;

import com.djl.springboot.bean.Car;
import com.djl.springboot.bean.Pet;
import com.djl.springboot.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author djl
 * @create 2021/1/20 16:01
 */
@Import({User.class})
@Configuration(proxyBeanMethods = true) //告诉SpringBoot这是一个配置类 == 配置文件
//@ConditionalOnBean(name = "tom")
//@ConditionalOnMissingBean(name = "tom")
//@ImportResource("classpath:beans.xml")
@EnableConfigurationProperties(Car.class)
//1、开启Car配置绑定功能
//2、把这个Car这个组件自动注册到容器中
public class MyConfig {


    /**
     * Full:外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象
     *
     * @return
     */

    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    @ConditionalOnMissingBean(name = {"tom"})
    public User user01() {
        User zhangsan = new User("zhangsan", 18);
        //user组件依赖了Pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("tom22")
    public Pet tomcatPet() {
        return new Pet("tomcat");
    }

//    @Bean
//    public CharacterEncodingFilter filter(){
//        return null;
//    }
}