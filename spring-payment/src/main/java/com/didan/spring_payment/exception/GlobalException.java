package com.didan.spring_payment.exception;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalException extends ResponseEntityExceptionHandler implements
    ResponseErrorHandler {

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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    return new ResponseEntity<>(new ResourceNotFound(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFound.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFound ex, WebRequest request) {
    return new ResponseEntity<>(new ResourceNotFound(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    HttpStatusCode status = response.getStatusCode();
    return status.is4xxClientError() || status.is5xxServerError();
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    String responseAsString = toString(response.getBody());
    throw new CustomException(responseAsString);
  }

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
    String responseAsString = toString(response.getBody());
    throw new CustomException(responseAsString);
  }

  private String toString(InputStream body) {
    Scanner s = new Scanner(body).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }
}
