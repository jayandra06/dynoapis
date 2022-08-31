package com.dynoapis.dams.model;

import lombok.Data;

@Data
public class Order {
    private int aggregator;
    private String aggname;
    private String orderno;
    private String restaurantid;
    private String token;
    private String discount;
    private String packingcharges;
}
