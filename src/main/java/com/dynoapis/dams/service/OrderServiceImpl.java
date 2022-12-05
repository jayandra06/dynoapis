package com.dynoapis.dams.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynoapis.dams.entity.OrderEntity;
import com.dynoapis.dams.exceptions.DBException;
import com.dynoapis.dams.exceptions.ResourceNotFoundException;
import com.dynoapis.dams.model.OrderRequest;
import com.dynoapis.dams.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Map<String, Object>> saveOrder(OrderRequest order) {
        List<Map<String, Object>> response = new ArrayList<>();
        order.getOrders().forEach(x->{
            OrderEntity orderEntity = new OrderEntity();
            Map<String, Object> record = new HashMap<>();
            orderEntity.setOrderId(x.getOrderId());
            orderEntity.setRestaurantId(x.getResId());
            orderEntity.setProcessed(false);
            orderEntity.setVendor(x.getVendor());
            orderEntity.setStatus(x.getStatus());
            orderEntity.setStatusCode(0);
            try {
                String jsonString = objectMapper.writeValueAsString(x.getData());
                orderEntity.setOrderJson(jsonString);
                try {
                    if (x.getOrderId() == null) {
                        record.put("message", "Order No. should not be null!");
                        record.put("status", 400);
                        record.put("orderId", "null");
                    } else if (orderRepository.existsByOrderId(x.getOrderId())) {
                        OrderEntity newUpdate = orderRepository.findByOrderId(x.getOrderId());
                        newUpdate.setStatus(x.getStatus());
                        orderRepository.save(newUpdate);
                        record.put("message", "Order No. " + x.getOrderId() + " Updated Successfully");
                        record.put("status", 200);
                        record.put("orderId", x.getOrderId());
                    }
                     else {
                        orderRepository.save(orderEntity);
                        record.put("message", "Order No. " + x.getOrderId() + " Inserted Successfully");
                        record.put("status", 200);
                        record.put("orderId", x.getOrderId());
                    }
                } catch(Exception e1) {
                    record.put("message", "Order No. " + x.getOrderId() + " is already inserted! Duplicate record is not Allowed");
                    record.put("status", 400);
                    record.put("orderId", x.getOrderId());
                }
                response.add(record);
            } catch (JsonProcessingException e) {
                throw new DBException("Invalid Request Body Sent");
            }   
        });
        return response; 
    }

    @Override
    public List<Object> getUnProcessedOrders(String resId, Timestamp startDate, Timestamp endDate) {
        List<OrderEntity> orderEntity = orderRepository.findByRestaurantIdAndCreatedAtBetween(resId, startDate, endDate);
        List<OrderEntity> filteredOrder = orderEntity.stream().filter(x->!x.isProcessed()).collect(Collectors.toList());
        filteredOrder.forEach(x->{
            x.setProcessed(true);
            orderRepository.save(x);
        });
       return getOrderJson(filteredOrder);
    }

    @Override
    public List<Object> getOrders(String resId, Timestamp startDate, Timestamp endDate) {
        List<OrderEntity> orderEntity = orderRepository.findByRestaurantIdAndCreatedAtBetween(resId, startDate, endDate);
        return getOrderJson(orderEntity);
    }

    @Override
    public List<Map<String, Object>> getOrdersByStatus(String restaurantId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<OrderEntity> orderEntity = orderRepository.findByRestaurantId(restaurantId);
        if (orderEntity.isEmpty()) {
            return list;
        }
        orderEntity.stream().forEach(x-> {
            Map<String, Object> data = new HashMap<>();
            if (x != null) {
                if (x.getStatusCode() > 0 && x.getStatusCode() < 4) {
                    data.put("orderId", x.getOrderId());
                    data.put("resId", x.getRestaurantId());
                    data.put("status", x.getStatusCode());
                    list.add(data);
                }
            }
        });
        return list;   
    }

    @Override
    public Map<String, Object> saveOrderStatus(String orderId, int statusCode, Map<String, Object> statusResponse) {
        Map<String, Object> map = new HashMap<>();
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        if (orderEntity != null) {
            orderEntity.setStatusCode(statusCode);
            try {
                String jsonString = objectMapper.writeValueAsString(statusResponse);
                orderEntity.setStatusResponse(jsonString);
                try {
                    orderRepository.save(orderEntity);
                } catch(Exception e1) {
                    throw new DBException(e1.getMessage());
                }
                orderRepository.save(orderEntity);
                map.put("status", statusCode);
                map.put("message", String.format("Updated the status to %d for order Id %s", statusCode, orderId));
            } catch (JsonProcessingException e) {
                throw new DBException("Invalid Request Body Sent");
            }   
        } else {
            throw new ResourceNotFoundException("Order ID " + orderId + " Not Found");
        }
        return map;
    }

    private List<Object> getOrderJson(List<OrderEntity> orderEntity) {
        return orderEntity.stream().map(x->{
            try {
                TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
                Map<String, Object> jsonString =  objectMapper.readValue(x.getOrderJson(), typeRef);
                Map<String, Object> order = new HashMap<>();
                order.put("data", jsonString);
                order.put("orderId", x.getOrderId());
                order.put("vendor", x.getVendor());
                order.put("resId", x.getRestaurantId());
                order.put("createdAt", x.getCreatedAt());
                order.put("updatedAt", x.getUpdatedAt());
                return order;
            } catch (JsonProcessingException e) {
                return new HashMap<String, String>();
            }
        }).collect(Collectors.toList());
    }
    
}
