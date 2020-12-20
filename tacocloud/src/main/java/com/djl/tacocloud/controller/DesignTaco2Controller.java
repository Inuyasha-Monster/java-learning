package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Taco;
import com.djl.tacocloud.repository.TacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.*;

import java.util.Optional;

/**
 * @author djl
 * @create 2020/12/20 15:18
 * @RestController注解有两个目的。首先，它是一个类似于@Controller和@Service的构造型注解，能够让类被组件扫描功能发现。但是，与REST最密切相关之处在于，@RestController注解会告诉Spring，控制器中的所有处理器方法的返回值都要直接写入响应体中，而不是将值放到模型中并传递给一个视图以便于进行渲染。 尽管这样会限制API是基于JSON的，但是我们还可以将produces设置为一个String类型的数组，这样的话就允许我们设置多个内容类型。比如，为了允许生成XML格式的输出，我们可以为produces属性添加“text/xml”：
 */
@RestController
@RequestMapping(value = "/design2", produces = {"application/json", "text/xml"})
//@RequestMapping注解还设置了一个produces属性。这指明DesignTacoController中的所有处理器方法只会处理Accept头信息包含“application/json”的请求。它不仅会限制API只会生成JSON结果，同时还允许其他的控制器（比如第2章中的DesignTacoController）处理具有相同路径的请求，只要这些请求不要求JSON格式的输出就可以。
@CrossOrigin(origins = "*")
public class DesignTaco2Controller {
    private TacoRepository tacoRepository;

    @Autowired
    public DesignTaco2Controller(TacoRepository tacoRepository) {
        this.tacoRepository = tacoRepository;
    }

    /**
     * 作为替代方案，我们也可以像其他Spring MVC控制器那样为DesignTacoController添加@Controller注解。但是，这样的话，我们就需要为每个处理器方法再添加@ResponseBody注解，这样才能达到相同的效果。另外一种方案就是返回ResponseEntity对象
     *
     * @return
     */
    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest);
    }

    @GetMapping("/recent2")
    public Iterable<Taco> recentTacos2() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//        org.springframework.hateoas.Links.
        return tacoRepository.findAll(pageRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> getTaocoById(@PathVariable("id") Long id) {
        Optional<Taco> result = tacoRepository.findById(id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * 我们设置了consumes属性。consumes属性用于指定请求输入，而produces用于指定请求输出。在这里，我们使用consumes属性，表明该方法只会处理Content-type与application/json相匹配的请求
     *
     * @param taco
     * @return
     */
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED) // 在POST请求的情况下，201 (CREATED)的HTTP状态更具有描述性。
    public Taco postTaco(@RequestBody Taco taco) { // 方法的Taco参数带有@RequestBody注解，表明请求应该被转换为一个Taco对象并绑定到该参数上。这个注解是非常重要的，如果没有它，Spring MVC将会认为我们希望将请求参数（要么是查询参数，要么是表单参数）绑定到Taco上。但是，@RequestBody注解能够确保请求体中的JSON会被绑定到Taco对象上。
        return tacoRepository.save(taco);
    }


}
