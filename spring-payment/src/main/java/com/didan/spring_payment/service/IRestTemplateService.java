package com.didan.spring_payment.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface IRestTemplateService {

  <T> ResponseEntity<T> process(HttpMethod httpMethod, String url, HttpHeaders httpHeaders,
      Object requestBody, Class<T> responseType);

  <T> ResponseEntity<T> processParameterizedType(HttpMethod httpMethod, String url,
      HttpHeaders httpHeaders,
      Object requestBody, ParameterizedTypeReference<T> parameterizedTypeReference);
}
