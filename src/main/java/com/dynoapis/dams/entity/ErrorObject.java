package com.dynoapis.dams.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorObject {
	
	private Integer status;
	
	private String message;
	
	private Date timestamp;
}
