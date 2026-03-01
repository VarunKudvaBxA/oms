package com.example.oms.service;
import com.example.oms.exception.InvalidOrderException;
import com.example.oms.model.*;
import com.example.oms.repository.OrderRepository;
import com.example.oms.util.OrderFileLogger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderFileLogger logger;

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public OrderService(OrderRepository repository, OrderFileLogger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public Order createOrder(String customer, OrderType type, double amount) {

        if (amount <= 0) {
            throw new InvalidOrderException("Amount must be positive");
        }

        Order order = new Order(customer, type, amount);
        repository.save(order);

        executor.submit(() -> processOrder(order));

        return order;
    }

    private void processOrder(Order order) {
        try {
            order.setStatus(OrderStatus.PROCESSING);
            Thread.sleep(2000);

            order.setStatus(OrderStatus.COMPLETED);
            logger.logOrder(order);

        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
        }
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public double getTotalAmount() {
        return repository.findAll()
                .stream()
                .mapToDouble(Order::getAmount)
                .sum();
    }

    public long getTotalBuyOrders() {
        return repository.findAll()
                .stream()
                .filter(o -> o.getType() == OrderType.BUY)
                .count();
    }

    public long getTotalSellOrders() {
        return repository.findAll()
                .stream()
                .filter(o -> o.getType() == OrderType.SELL)
                .count();
    }

    public String getTopCustomer() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomerName,
                        Collectors.summingDouble(Order::getAmount)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Orders");
    }

    public Map<OrderStatus, List<Order>> groupByStatus() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Order::getStatus));
    }
}