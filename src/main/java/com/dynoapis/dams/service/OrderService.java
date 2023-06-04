package com.dynoapis.dams.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.dynoapis.dams.model.OrderRequest;

public interface OrderService {
    
    List<Map<String, Object>> saveOrder(OrderRequest order);

    List<Object> getUnProcessedOrders(String resId, Timestamp startDate, Timestamp endDate);

    List<Object> getOrders(String resId, Timestamp startDate, Timestamp endDate);
    List<Object> getAllOrders(Timestamp startDate, Timestamp endDate);

    Map<String, Object> getOrdersByStatus(String restaurantId);

    Map<String, Object> saveOrderStatus(String orderId, int statusCode, Map<String, Object> statusResponse);

    Map<String, Object> saveOrderHistory(String resId, Map<String, Object> json);

}
