# 关于Spring5实战笔记摘抄以及项目说明

你可能已经看出来了，这就是一个典型的Maven或Gradle项目结构，其中应用的源码放到了“src/main/java”中，测试代码放到了“src/test/java”中，而非Java的资源放到了“src/main/resources”。

在这个项目结构中，我们需要注意以下几点。
mvnw和mvnw.cmd：这是Maven包装器（wrapper）脚本。借助这些脚本，即便你的机器上没有安装Maven，也可以构建项目。
pom.xml：这是Maven构建规范，随后我们将会深入介绍该文件。
TacoCloudApplication.java：这是Spring Boot主类，它会启动该项目。随后，我们会详细介绍这个类。
application.properties：这个文件起初是空的，但是它为我们提供了指定配置属性的地方。在本章中，我们会稍微修改一下这个文件，但是我会将配置属性的详细阐述放到第5章。
static：在这个文件夹下，你可以存放任意为浏览器提供服务的静态内容（图片、样式表、JavaScript等），该文件夹初始为空。
templates：这个文件夹中存放用来渲染内容到浏览器的模板文件。这个文件夹初始是空的，不过我们很快就会往里面添加Thymeleaf模板。
TacoCloudApplicationTests.java：这是一个简单的测试类，它能确保Spring应用上下文可以成功加载。在开发应用的过程中，我们会将更多的测试添加进来。


@SpringBootApplication是一个组合注解，它组合了3个其他的注解。
•@SpringBootConfiguration：将该类声明为配置类。尽管这个类目前还没有太多的配置，但是后续我们可以按需添加基于Java的Spring框架配置。这个注解实际上是@Configuration注解的特殊形式。
•@EnableAutoConfiguration：启用Spring Boot的自动配置。我们随后会介绍自动配置的更多功能。就现在来说，我们只需要知道这个注解会告诉Spring Boot自动配置它认为我们会用到的组件。
•@ComponentScan：启用组件扫描。这样我们能够通过像@Component、@Controller、@Service这样的注解声明其他类，Spring会自动发现它们并将它们注册为Spring应用上下文中的组件。


JSP并不需要在构建文件中添加任何特殊的依赖。这是因为Servlet容器本身（默认是Tomcat）会实现JSP，因此不需要额外的依赖。但是，如果你选择使用JSP，会有另外一个问题。事实上，Java Servlet容器包括嵌入式的Tomcat和Jetty容器，通常会在“/WEB-INF”目录下寻找JSP。如果我们将应用构建成一个可执行的JAR文件，就无法满足这种需求了
因此，只有在将应用构建为WAR文件并部署到传统的Servlet容器中时，才能选择JSP方案。如果你想要构建可执行的JAR文件，那么必须选择Thymeleaf、FreeMarker或表2.2中的其他方案。

•Spring提供了一个强大的Web框架，名为Spring MVC，能够用来为Spring应用开发Web前端。
•Spring MVC是基于注解的，通过像@RequestMapping、@GetMapping和@PostMapping这样的注解来启用请求处理方法的声明。
•大多数的请求处理方法最终会返回一个视图的逻辑名称，比如Thymeleaf模板，请求会转发到这样的视图上（同时会带有任意的模型数据）。
•Spring MVC支持校验，这是通过Java Bean Validation API和Validation API的实现（如Hibernate Validator）完成的。
•对于没有模型数据和逻辑处理的HTTP GET请求，可以使用视图控制器。
•除了Thymeleaf之外，Spring支持各种视图方案，包括FreeMarker、Groovy Templates和Mustache。