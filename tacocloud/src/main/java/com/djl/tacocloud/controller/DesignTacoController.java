package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Ingredient;
import com.djl.tacocloud.entity.Ingredient.Type;
import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.Taco;
import com.djl.tacocloud.repository.IngredientRepository;
import com.djl.tacocloud.repository.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author djl
 * @create 2020/12/18 11:29
 * @Slf4j -> 为类提供一个 属性名为log 的 log4j 日志对像
 * 类级别的@SessionAttributes能够指定模型对象（如订单属性）要保存在session中，这样才能跨请求使用。
 */
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping
    public String showDesignForm2(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        // 读取数据库数据加载
        ingredientRepository.findAll().forEach(x -> ingredients.add(x));
        final Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    //    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient(" CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient(" SRCR", "Sour Cream", Type.SAUCE)
        );
        final Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
        model.addAttribute("design", new Taco());
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }

    /**
     * 如果从session找不到实例则调用此方法创建对象
     * @return
     */
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @PostMapping
    public String processDesign2(@Valid Taco design, Errors errors, @ModelAttribute Order order, Model model) { // Order参数带有@ModelAttribute注解，表明它的值应该是来自模型的，SpringMVC不会尝试将请求参数绑定到它上面。
        if (errors.hasErrors()) {
            // 如果存在校验错误，那么这个方法将不会处理Taco对象并返回“design”视图名，表单会重新展现。
            model.addAttribute("design", design);
            return "design";
        }
        // @Valid注解会告诉Spring MVC要对提交的Taco对象进行校验，而校验时机是在它绑定完表单数据之后、调用processDesign()之前
        // 如果存在校验错误，那么这些错误的细节将会捕获到一个Errors对象中并传递给processDesign()。
//        log.info("Processing design: " + design);
        final Taco taco = tacoRepository.save(design);
        // 在检查完校验错误之后，processDesign()使用注入的TacoRepository来保存taco。然后，它将Taco对象保存到session里面的Order中。
        // 实际上，在用户完成操作并提交订单表单之前，Order对象会一直保存在session中，并没有保存到数据库中
        order.addDesign(taco);
        return "redirect:/orders/current";
    }

    //    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors) {
        if (errors.hasErrors()) {
            // 如果存在校验错误，那么这个方法将不会处理Taco对象并返回“design”视图名，表单会重新展现。
            return "design";
        }
        // @Valid注解会告诉Spring MVC要对提交的Taco对象进行校验，而校验时机是在它绑定完表单数据之后、调用processDesign()之前
        // 如果存在校验错误，那么这些错误的细节将会捕获到一个Errors对象中并传递给processDesign()。
        log.info("Processing design: " + design);
        return "redirect:/orders/current";
    }
}
