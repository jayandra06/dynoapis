package com.dynoapis.dams.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynoapis.dams.model.Item;
import com.dynoapis.dams.service.ItemService;

@RestController
@CrossOrigin(origins = "*")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/api/v1/{restaurant_id}/items")
    public Map<String, Object> getItemsAndCategories(@PathVariable String restaurant_id) {
        return itemService.getItemsAndCategories(restaurant_id);
    }

    @PostMapping("/api/v1/{restaurant_id}/items")
    public Map<String, Object> saveItems(@PathVariable String restaurant_id, @RequestBody Map<String, Object> json) {
        return itemService.saveItemAggregatorStatus(restaurant_id, json);
    }

    @PostMapping("/api/v1/{restaurant_id}/items/status")
    public Map<String, Object> saveItem(@PathVariable String restaurant_id, @RequestBody Item item) {
        return itemService.insertItem(restaurant_id, item);
    }

    @PostMapping("/api/v1/{restaurant_id}/categories/status")
    public Map<String, Object> saveCategories(@PathVariable String restaurant_id, @RequestBody Item category) {
        return itemService.insertCategory(restaurant_id, category);
    }

}
