package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Ingredient;
import com.djl.tacocloud.entity.Ingredient.Type;
import com.djl.tacocloud.entity.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author djl
 * @create 2020/12/18 11:29
 * @Slf4j -> 为类提供一个 属性名为log 的 log4j 日志对像
 */
@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {
    @GetMapping
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

    @PostMapping
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
