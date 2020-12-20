package com.djl.tacocloud.controller;

import com.djl.tacocloud.entity.Order;
import com.djl.tacocloud.entity.User;
import com.djl.tacocloud.repository.OrderRepository;
import com.djl.tacocloud.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.security.Principal;

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
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user, Model model) {
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user));
        return "orderList";
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
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @ModelAttribute("order") Order sessionOrder,
                               Principal principal,
                               Authentication authentication,
                               @AuthenticationPrincipal User user) { //@AuthenticationPrincipal非常好的一点在于它不需要类型转换（前文中的Authentication则需要进行类型转换）
        if (errors.hasErrors()) {
            return "orderForm";
        }
        // 妈的这里的 sessionOrder 对象里面的taco集合为空 , 暂不知道原因
        if (sessionOrder != null) {
            sessionOrder.getTacos().stream().forEach(x -> order.addDesign(x));
        }

//        User user = userRepository.findUserByUsername(principal.getName());
//        order.setUser(user);

//        User user = (User) authentication.getPrincipal();
        order.setUser(user);

        // 它可以在应用程序的任何地方使用，而不仅仅是在控制器的处理器方法中。这使得它非常适合在较低级别的代码中使用。
//        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
//        User user1 = (User) authentication1.getPrincipal();

        orderRepository.save(order);
        // 订单保存完成之后，我们就不需要在session中持有它了。实际上，如果我们不把它清理掉，那么订单会继续保留在session中，其中包括与之关联的taco，下一次的订单将会从旧订单中保存的taco开始。所以，processOrder()方法请求了一个SessionStatus参数，并调用了它的setComplete()方法来重置session。
        sessionStatus.setComplete();
        log.info("Order submitted: " + order);
        return "redirect:/";
    }
}
