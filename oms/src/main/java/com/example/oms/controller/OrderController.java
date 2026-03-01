package com.example.oms.controller;

import com.example.oms.model.Order;
import com.example.oms.model.OrderType;
import com.example.oms.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order create(
            @RequestParam String customer,
            @RequestParam OrderType type,
            @RequestParam double amount) {

        return service.createOrder(customer, type, amount);
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAllOrders();
    }

    @GetMapping("/analytics/total")
    public double totalAmount() {
        return service.getTotalAmount();
    }

    @GetMapping("/analytics/top-customer")
    public String topCustomer() {
        return service.getTopCustomer();
    }

    @GetMapping("/analytics/group-status")
    public Map<?, ?> groupStatus() {
        return service.groupByStatus();
    }
}