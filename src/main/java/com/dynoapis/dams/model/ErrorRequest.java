package com.dynoapis.dams.model;

import lombok.Data;

@Data
public class ErrorRequest {

  private String errorMessage;
  private String resId;
  private String accessToken;

}