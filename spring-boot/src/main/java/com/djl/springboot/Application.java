package com.djl.springboot;

import com.djl.springboot.bean.Pet;
import com.djl.springboot.bean.User;
import com.djl.springboot.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);

        //2、查看容器里面的组件
//        String[] names = run.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }

        boolean tom = run.containsBean("tom22");
        System.out.println("容器中Tom组件：" + tom);

        boolean user01 = run.containsBean("user01");
        System.out.println("容器中user01组件：" + user01);

//        boolean tom22 = run.containsBean("tom22");
//        System.out.println("容器中tom22组件：" + tom22);

//
//        //3、从容器中获取组件
////
//        Pet tom01 = run.getBean("tom", Pet.class);
//
//        Pet tom02 = run.getBean("tom", Pet.class);
//
//        System.out.println("tom组件：" + (tom01 == tom02));
//////
//////
////        //4、com.atguigu.boot.config.MyConfig$$EnhancerBySpringCGLIB$$51f1e1ca@1654a892
//        MyConfig bean = run.getBean(MyConfig.class);
//        System.out.println(bean);
//
//        final Pet pet = bean.tomcatPet();
//        final Pet pet1 = bean.tomcatPet();
//        System.out.println("pet:" + (pet == pet1));
//
//
////
////        //如果@Configuration(proxyBeanMethods = true)代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有。
////        //保持组件单实例
//        User user = bean.user01();
//        User user1 = bean.user01();
//        System.out.println("user:" + (user == user1));
//        System.out.println("user pet:" + (user.getPet() == user1.getPet()));
////
//////
//        User user01 = run.getBean("user01", User.class);
//        Pet tom = run.getBean("tom", Pet.class);
//
//        System.out.println("用户的宠物：" + (user01.getPet() == tom));
//
//        //5、获取组件
//        String[] beanNamesForType = run.getBeanNamesForType(User.class);
//        System.out.println("======");
//        for (String s : beanNamesForType) {
//            System.out.println(s);
//        }

    }

}
