package com.dynoapis.dams.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynoapis.dams.entity.ItemEntity;
import com.dynoapis.dams.entity.ItemsStatusEntity;
import com.dynoapis.dams.exceptions.DBException;
import com.dynoapis.dams.model.Item;
import com.dynoapis.dams.repository.ItemRepository;
import com.dynoapis.dams.repository.ItemsStatusRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemsStatusRepository itemsStatusRepository;

    public Map<String, Object> insertItem(String resId, Item item) {
        Map<String, Object> response = new HashMap<>();
        ItemEntity itemEntity = itemRepository.findByRestaurantIdAndEntityId(resId, item.getEntityId());
        if (itemEntity == null) {
            itemEntity = new ItemEntity();
            itemEntity.setAggregator(item.getAggregator());
            itemEntity.setIsStatus(item.getStockStatus());
            itemEntity.setRestaurantId(resId);
            itemEntity.setType("ITEM");
            itemEntity.setEntityId(item.getEntityId());
            itemEntity.setIsProcessed(item.getIsProcessed());
        } else {
            itemEntity.setAggregator(item.getAggregator());
            itemEntity.setIsStatus(item.getStockStatus());
            itemEntity.setRestaurantId(resId);
            itemEntity.setType("ITEM");
            itemEntity.setIsProcessed(item.getIsProcessed());
        }
        try {
            String jsonResponse = objectMapper.writeValueAsString(item.getStatusResponse());
            itemEntity.setStatusResponse(jsonResponse);
            itemRepository.save(itemEntity);
        } catch (JsonProcessingException e) {
            throw new DBException(e.getMessage());
        }
        response.put("status", 200);
        response.put("message", "Stock for Item ID " + item.getEntityId() + " Updated Successfully");
        return response;
    }

    public Map<String, Object> insertCategory(String resId, Item category) {
        Map<String, Object> response = new HashMap<>();
        ItemEntity itemEntity = itemRepository.findByRestaurantIdAndEntityId(resId, category.getEntityId());
        if (itemEntity == null) {
            itemEntity = new ItemEntity();
            itemEntity.setAggregator(category.getAggregator());
            itemEntity.setIsStatus(category.getStockStatus());
            itemEntity.setRestaurantId(resId);
            itemEntity.setType("CATEGORY");
            itemEntity.setIsProcessed(category.getIsProcessed());
            itemEntity.setEntityId(category.getEntityId());
        } else {
            itemEntity.setAggregator(category.getAggregator());
            itemEntity.setIsStatus(category.getStockStatus());
            itemEntity.setRestaurantId(resId);
            itemEntity.setIsProcessed(category.getIsProcessed());
            itemEntity.setType("CATEGORY");
        }
        try {
            String jsonResponse = objectMapper.writeValueAsString(category.getStatusResponse());
            itemEntity.setStatusResponse(jsonResponse);
            itemRepository.save(itemEntity);
        } catch (JsonProcessingException e) {
            throw new DBException(e.getMessage());
        }
        response.put("status", 200);
        response.put("message", "Stock for Category ID " + category.getEntityId() + " Updated Successfully");
        return response;
    }


    @Override
    public Map<String, Object> getItemsAndCategories(String resId) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Map<String, Object>> items = new ArrayList<>();;
        response.put("restaurantId", resId);
        List<ItemEntity> itemEntities = itemRepository.findByRestaurantId(resId);
        ItemsStatusEntity itemsStatusEntity = itemsStatusRepository.findByRestaurantId(resId);
        if(itemEntities.isEmpty()) {
            response.put("getAllItems", (itemsStatusEntity != null) ? itemsStatusEntity.getRequestedStatus() : false);
            response.put("categories", categories);
            response.put("items", items);
            return response;
        }
        itemEntities.forEach(x-> {
            if(!x.getIsProcessed()) {
                if(x.getType().equalsIgnoreCase("category")) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("id", x.getEntityId());
                    category.put("stockStatus", x.getIsStatus());
                    category.put("aggregator", x.getAggregator());
                    categories.add(category); 
                }
                if(x.getType().equalsIgnoreCase("item")) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", x.getEntityId());
                    item.put("stockStatus", x.getIsStatus());
                    item.put("aggregator", x.getAggregator());
                    items.add(item); 
                }
            }
        });
        response.put("getAllItems", (itemsStatusEntity != null) ? itemsStatusEntity.getRequestedStatus() : false);
        response.put("categories", categories);
        response.put("items", items);
        return response;
    }

    public Map<String, Object> saveItemAggregatorStatus(String resId, Map<String, Object> json) {
        ItemsStatusEntity itemsStatusEntity = itemsStatusRepository.findByRestaurantId(resId);
        if(itemsStatusEntity == null) {
            itemsStatusEntity = new ItemsStatusEntity();
            itemsStatusEntity.setRestaurantId(resId);
        }
        if (json.containsKey("status")) {
            itemsStatusEntity.setRequestedStatus(Boolean.valueOf(json.get("status").toString().trim()));
        } else
            itemsStatusEntity.setRequestedStatus(false);
        String jsonString;
        try {
            if (json.containsKey("statusResponse"))
                jsonString = objectMapper.writeValueAsString(json.get("statusResponse"));
            else 
                jsonString = "";
            itemsStatusEntity.setItemsJson(jsonString);
            itemsStatusRepository.save(itemsStatusEntity);
        } catch (JsonProcessingException e) {
            throw new DBException(e.getMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Get Items Status Updated Successfully");
        return response;
    }
    
}
