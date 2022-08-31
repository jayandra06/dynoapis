package com.dynoapis.dams.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynoapis.dams.entity.OrderEntity;
import com.dynoapis.dams.exceptions.DBException;
import com.dynoapis.dams.model.OrderRequest;
import com.dynoapis.dams.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> saveOrder(OrderRequest order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setResTokenId(String.format("%s_%s",
                             order.getOrder().getToken(),
                             order.getOrder().getRestaurantid()));
        orderEntity.setAccessToken(order.getOrder().getToken());
        orderEntity.setRestaurantId(order.getOrder().getRestaurantid());
        orderEntity.setProcessed(false);
        orderEntity.setOrderId(order.getOrder().getOrderno());
        Map<String, Object> response = new HashMap<>();
        try {
            String jsonString = objectMapper.writeValueAsString(order);
            orderEntity.setOrderJson(jsonString);
            try {
                orderRepository.save(orderEntity);
            } catch(Exception e1) {
                throw new DBException("Order No. "+ order.getOrder().getOrderno() + " is already inserted! Duplicate record is not Allowed");
            }
            response.put("message", "Order No. " + order.getOrder().getOrderno() + " Inserted Successfully");
            response.put("status", 200);
            return response;
        } catch (JsonProcessingException e) {
            throw new DBException("Invalid Request Body Sent");
        }                        
    }

    @Override
    public List<Object> getUnProcessedOrders(String orderId, Timestamp startDate, Timestamp endDate) {
        List<OrderEntity> orderEntity = orderRepository.findByResTokenIdAndCreatedAtBetween(orderId, startDate, endDate);
        List<OrderEntity> filteredOrder = orderEntity.stream().filter(x->!x.isProcessed()).collect(Collectors.toList());
        filteredOrder.forEach(x->{
            x.setProcessed(true);
            orderRepository.save(x);
        });
       return getOrderJson(filteredOrder);
    }

    @Override
    public List<Object> getOrders(String orderId, Timestamp startDate, Timestamp endDate) {
        List<OrderEntity> orderEntity = orderRepository.findByResTokenIdAndCreatedAtBetween(orderId, startDate, endDate);
        return getOrderJson(orderEntity);
    }

    private List<Object> getOrderJson(List<OrderEntity> orderEntity) {
        return orderEntity.stream().map(x->{
            try {
                OrderRequest jsonString =  objectMapper.readValue(x.getOrderJson(), OrderRequest.class);
                jsonString.setCreatedAt(x.getCreatedAt());
                return jsonString;
            } catch (JsonProcessingException e) {
                return new HashMap<String, String>();
            }
        }).collect(Collectors.toList());
    }
    
}
