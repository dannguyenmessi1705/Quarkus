package com.didan.spring_payment.exception;

import java.io.IOException;

public class CustomException extends IOException {

  public CustomException(String message) {
    super(message);
  }
}
