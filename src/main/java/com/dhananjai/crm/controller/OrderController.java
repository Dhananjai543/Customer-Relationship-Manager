package com.dhananjai.crm.controller;

import com.dhananjai.crm.entity.Customer;
import com.dhananjai.crm.entity.Order;
import com.dhananjai.crm.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
@Tag(name = "Order APIs", description = "APIs for managing customer orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    @Operation(summary = "List all orders for a customer", description = "Retrieves all orders associated with a specific customer.")
    @Parameter(name = "customerId", description = "ID of the customer whose orders are to be listed", required = true)
    public String listOrder(@RequestParam("customerId") int theId, Model theModel) {

        List<Order> theOrders = orderRepository.findByCustId(theId);

        theModel.addAttribute("orders",theOrders);
        theModel.addAttribute("customerId",theId);
        System.out.println("Inside /orders : listOrder");
        return "list-orders";
    }

    @GetMapping("/showFormForAdd")
    @Operation(summary = "Show form for adding a new order", description = "Displays the order creation form for a specific customer.")
    @Parameter(name = "customerId", description = "ID of the customer for whom the order is being created", required = true)
    public String showFormForAdd(@RequestParam("customerId") int theId, Model theModel) {

        Order theOrder = new Order();
        theOrder.setCustId(theId);
        theModel.addAttribute("order",theOrder);

        return "order-form";
    }

    @GetMapping("/showFormForDelete")
    @Operation(summary = "Show form for deleting an order", description = "Displays the order deletion form for a specific order.")
    @Parameter(name = "orderId", description = "ID of the order to be deleted", required = true)
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
    @Operation(summary = "Save an order", description = "Saves a new or updated order to the database.")
    public String saveOrder(@ModelAttribute("order") Order theOrder, Model model) {

        orderRepository.save(theOrder);

        return "redirect:/order/list?customerId="+theOrder.getCustId();
    }

    @GetMapping("/showFormForUpdate")
    @Operation(summary = "Show update form", description = "Displays the form for updating a specific order.")
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
    @Operation(summary = "Delete order", description = "Deletes an order from the database.")
    public String deleteOrder(@ModelAttribute("order") Order theOrder) {

        //delete the order
        orderRepository.deleteById(theOrder.getId());
        return "redirect:/order/list?customerId="+theOrder.getCustId();
    }
}
