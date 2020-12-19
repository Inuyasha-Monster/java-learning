package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

/**
 * @author djl
 * @create 2020/12/18 15:18
 */
@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

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

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    /**
     * 处理订单请求
     *
     * @param order
     * @return
     */
    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @ModelAttribute("order") Order sessionOrder) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        // 妈的这里的 sessionOrder 对象里面的taco集合为空 , 暂不知道原因
        if (sessionOrder != null) {
            sessionOrder.getTacos().stream().forEach(x -> order.addDesign(x));
        }
        orderRepository.save(order);
        // 订单保存完成之后，我们就不需要在session中持有它了。实际上，如果我们不把它清理掉，那么订单会继续保留在session中，其中包括与之关联的taco，下一次的订单将会从旧订单中保存的taco开始。所以，processOrder()方法请求了一个SessionStatus参数，并调用了它的setComplete()方法来重置session。
        sessionStatus.setComplete();
        log.info("Order submitted: " + order);
        return "redirect:/";
    }
}
