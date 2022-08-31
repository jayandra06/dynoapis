package com.dynoapis.dams.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.dynoapis.dams.model.OrderRequest;

public interface OrderService {
    
    Map<String, Object> saveOrder(OrderRequest order);

    List<Object> getUnProcessedOrders(String orderId, Timestamp startDate, Timestamp endDate);

    List<Object> getOrders(String orderId, Timestamp startDate, Timestamp endDate);

}
