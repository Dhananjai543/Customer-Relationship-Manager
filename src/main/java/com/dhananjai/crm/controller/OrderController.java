package com.dhananjai.crm.controller;

import com.dhananjai.crm.entity.Customer;
import com.dhananjai.crm.entity.Order;
import com.dhananjai.crm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    public String listOrder(@RequestParam("customerId") int theId, Model theModel) {

        List<Order> theOrders = orderRepository.findByCustId(theId);

        theModel.addAttribute("orders",theOrders);
        theModel.addAttribute("customerId",theId);
        System.out.println("Inside /orders : listOrder");
        return "list-orders";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@RequestParam("customerId") int theId, Model theModel) {

        Order theOrder = new Order();
        theOrder.setCustId(theId);
        theModel.addAttribute("order",theOrder);

        return "order-form";
    }

    @GetMapping("/showFormForDelete")
    public String showFormForDelete(@RequestParam("orderId") int theId, Model theModel) {

        Optional<Order> theOrder = orderRepository.findById(theId);

        if (theOrder.isPresent()) {
            theModel.addAttribute("order", theOrder.get());
        } else {
            theModel.addAttribute("error", "Order not found");
        }

        //send over to our form
        return "delete-order-form";
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute("order") Order theOrder, Model model) {

        orderRepository.save(theOrder);

        return "redirect:/order/list?customerId="+theOrder.getCustId();
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("orderId") int theId, Model theModel) {

        Optional<Order> theOrder = orderRepository.findById(theId);
        if (theOrder.isPresent()) {
            theModel.addAttribute("order", theOrder.get());
        } else {
            theModel.addAttribute("error", "Order not found");
        }

        //send over to our form
        return "order-form";
    }

    @PostMapping("/delete")
    public String deleteOrder(@ModelAttribute("order") Order theOrder) {

        //delete the order
        orderRepository.deleteById(theOrder.getId());
        return "redirect:/order/list?customerId="+theOrder.getCustId();
    }
}
