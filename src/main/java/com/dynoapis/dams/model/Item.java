package com.dynoapis.dams.model;

import lombok.Data;

@Data
public class Item {
    private String item;
    private int qty;
    private String amount;
    private String variant;
    private String positemcode;
    private String aggitemcode;
}
