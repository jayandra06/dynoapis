package com.dynoapis.dams.controllers;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dynoapis.dams.entity.ErrorEntity;
import com.dynoapis.dams.exceptions.DBException;
import com.dynoapis.dams.model.ErrorRequest;
import com.dynoapis.dams.model.OrderRequest;
import com.dynoapis.dams.model.OrderStatus;
import com.dynoapis.dams.repository.ErrorRepository;
import com.dynoapis.dams.service.OrderService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ErrorRepository errorRepository;
        
    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Server is up and running");
        return response;
    }

    @PostMapping("/api/v1/orders")
    public List<Map<String, Object>> insertNewOrders(@RequestBody OrderRequest order) {
        return orderService.saveOrder(order);
    }

    @GetMapping("/api/v1/{restaurantId}/orders")
    public List<Object> getOrders(@PathVariable String restaurantId) {
        Calendar startToday = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("IST");
        startToday.setTimeZone(tz);
        startToday.add(Calendar.HOUR_OF_DAY, -25);
        startToday.set(Calendar.MINUTE, 0);
        startToday.set(Calendar.SECOND, 0);
        startToday.set(Calendar.MILLISECOND, 0);
        Timestamp startDate = new Timestamp(startToday.getTimeInMillis());
        Timestamp endDate = new Timestamp(System.currentTimeMillis());
        return orderService.getOrders(restaurantId, startDate, endDate);
    }

    @GetMapping("/api/v1/orders")
    public List<Object> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "0") int size ) {
        System.out.println(page);
        System.out.println(size);
        if(page == 0 || size == 0) {
            Calendar startToday = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("IST");
            startToday.setTimeZone(tz);
            startToday.add(Calendar.HOUR_OF_DAY, -168);
            startToday.set(Calendar.MINUTE, 0);
            startToday.set(Calendar.SECOND, 0);
            startToday.set(Calendar.MILLISECOND, 0);
            Timestamp startDate = new Timestamp(startToday.getTimeInMillis());
            Timestamp endDate = new Timestamp(System.currentTimeMillis());
            return orderService.getAllOrders(startDate, endDate);
        } else {
            return orderService.getAllOrders(page, size);
        }
    }

    @GetMapping("/api/v1/{restaurantId}/orders/status")
    public Map<String, Object> getActionsToBePerformed(@PathVariable String restaurantId) {
        return orderService.getOrdersByStatus(restaurantId);
    }


    @PostMapping("/api/v1/{restaurantId}/orders/history")
    public Map<String, Object> insertOrderHistory(@PathVariable String restaurantId, @RequestBody Map<String, Object> json) {
        return orderService.saveOrderHistory(restaurantId, json);
    }

    @PostMapping("/api/v1/orders/{orderId}/status")
    public Map<String, Object> getActionsPerformed(@PathVariable String orderId, @RequestBody OrderStatus order) {
        return orderService.saveOrderStatus(orderId, order.getStatusCode(), order.getStatusResponse());
    }


    @PostMapping("/api/v1/errors")
    public Map<String, Object> sendErrors(@RequestBody ErrorRequest error) {
        try {
            ErrorEntity errorEntity = new ErrorEntity();
            errorEntity.setAccessToken(error.getAccessToken());
            errorEntity.setErrorMessage(error.getErrorMessage());
            errorEntity.setResId(error.getResId());
            errorRepository.save(errorEntity);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Inserted Successfully!");
        return response;
    }

}
