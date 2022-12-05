package com.dynoapis.dams.model;

import java.util.Map;

import lombok.Data;

@Data
public class Order {
    private String vendor;
    private String orderId;
    private String resId;
    private String status;
    private Map<String, Object> data;
}
