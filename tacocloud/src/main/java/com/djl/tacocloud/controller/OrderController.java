package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author djl
 * @create 2020/12/18 15:18
 */
@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    /**
     * 初始化下单页面
     *
     * @param model
     * @return
     */
    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    /**
     * 处理订单请求
     *
     * @param order
     * @return
     */
    @PostMapping
    public String processOrder(@Valid Order order, Errors errors) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + order);
        return "redirect:/";
    }
}
