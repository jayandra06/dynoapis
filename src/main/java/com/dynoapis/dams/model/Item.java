package com.dynoapis.dams.model;

import java.util.Map;

import lombok.Data;

@Data
public class Item {
    private String entityId;
    private Boolean stockStatus;
    private Boolean isProcessed;
    private String aggregator;
    private Map<String, Object> statusResponse;
}
