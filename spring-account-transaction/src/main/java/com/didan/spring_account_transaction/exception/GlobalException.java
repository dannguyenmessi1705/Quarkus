package com.didan.spring_account_transaction.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    return new ResponseEntity<>(new ResourceNotFound(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFound.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFound ex, WebRequest request) {
    return new ResponseEntity<>(new ResourceNotFound(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Map<String, String> validationErrors = new HashMap<>(); // Tạo một Map để chứa thông báo lỗi
    List<ObjectError> validationErrorsList = ex.getBindingResult().getAllErrors(); // Lấy ra tất cả các lỗi từ MethodArgumentNotValidException object

    validationErrorsList.forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
  }
}
