package com.example.oms.util;
import com.example.oms.model.Order;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class OrderFileLogger {

    public void logOrder(Order order) {
        try (FileWriter writer = new FileWriter("orders.log", true)) {
            writer.write(order.toString() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

