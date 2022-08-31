package com.dynoapis.dams.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private Order order;
    private List<Item> items;
    Timestamp createdAt;
    Timestamp updatedAt;
}
