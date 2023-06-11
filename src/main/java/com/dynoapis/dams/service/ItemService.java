package com.dynoapis.dams.service;

import java.util.Map;

import com.dynoapis.dams.model.Item;

public interface ItemService {

    public Map<String, Object> insertItem(String resId, Item item);
    public Map<String, Object> insertCategory(String resId, Item item);
    public Map<String, Object> getItemsAndCategories(String resId);
    public Map<String, Object> saveItemAggregatorStatus(String resId, Map<String, Object> json);
    
}
