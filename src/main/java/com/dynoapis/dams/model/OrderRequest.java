package com.dynoapis.dams.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private List<Order> orders;
    Timestamp createdAt;
    Timestamp updatedAt;
}
