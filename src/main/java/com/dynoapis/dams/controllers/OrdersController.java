package com.dynoapis.dams.controllers;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dynoapis.dams.model.OrderRequest;
import com.dynoapis.dams.service.OrderService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {

    @Autowired
    private OrderService orderService;
    
    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Server is up and running");
        return response;
    }

    @PostMapping("/neworder")
    public Map<String, Object> insertNewOrder(@RequestBody OrderRequest order) {
        return orderService.saveOrder(order);
    }

    @GetMapping("/{restaurantId}/order")
    public List<Object> getOrder(@RequestParam String token, @PathVariable String restaurantId) {
        Calendar startToday = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("IST");
        startToday.setTimeZone(tz);
        startToday.add(Calendar.HOUR_OF_DAY, -24);
        startToday.set(Calendar.MINUTE, 0);
        startToday.set(Calendar.SECOND, 0);
        startToday.set(Calendar.MILLISECOND, 0);
        Timestamp startDate = new Timestamp(startToday.getTimeInMillis());
        return orderService.getUnProcessedOrders(String.format("%s_%s", token, restaurantId ), startDate, new Timestamp(System.currentTimeMillis()));
    }

    @GetMapping("/{restaurantId}/orders")
    public List<Object> getOrders(@RequestParam String token, @PathVariable String restaurantId) {
        Calendar startToday = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("IST");
        startToday.setTimeZone(tz);
        startToday.add(Calendar.HOUR_OF_DAY, -24);
        startToday.set(Calendar.MINUTE, 0);
        startToday.set(Calendar.SECOND, 0);
        startToday.set(Calendar.MILLISECOND, 0);
        Timestamp startDate = new Timestamp(startToday.getTimeInMillis());
        Timestamp endDate = new Timestamp(System.currentTimeMillis());
        return orderService.getOrders(String.format("%s_%s", token, restaurantId ), startDate, endDate);
    }


}
