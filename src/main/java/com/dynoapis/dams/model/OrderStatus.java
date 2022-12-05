package com.dynoapis.dams.model;

import java.util.Map;

import lombok.Data;

@Data
public class OrderStatus {
    private Map<String, Object> statusResponse;
    private int statusCode;
}
