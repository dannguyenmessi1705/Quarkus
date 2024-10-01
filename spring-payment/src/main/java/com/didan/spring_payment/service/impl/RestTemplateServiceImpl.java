package com.didan.spring_payment.service.impl;

import com.didan.spring_payment.exception.ResourceNotFound;
import com.didan.spring_payment.service.IRestTemplateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestTemplateServiceImpl implements IRestTemplateService {
  private final RestTemplate restTemplate;

  @Override
  public <T> ResponseEntity<T> process(HttpMethod httpMethod, String url, HttpHeaders httpHeaders,
      Object requestBody, Class<T> responseType) {
    if(httpHeaders == null) {
      httpHeaders = new HttpHeaders();
      httpHeaders.put("Content-Type", List.of("application/json"));
      httpHeaders.put("Accept", List.of("application/json"));
    }
    try {
      ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestBody, httpHeaders), responseType);
      return responseEntity;
    } catch (HttpClientErrorException | HttpServerErrorException ex) {
      throw new ResourceNotFound(ex.getMessage());
    } catch (ResourceAccessException ex) {
      log.error("Connection timeout");
      throw new ResourceNotFound(ex.getMessage());
    }
  }

  @Override
  public <T> ResponseEntity<T> processParameterizedType(HttpMethod httpMethod, String url,
      HttpHeaders httpHeaders, Object requestBody,
      ParameterizedTypeReference<T> parameterizedTypeReference) {
    if (httpHeaders == null) {
      httpHeaders = new HttpHeaders();
      httpHeaders.put("Content-Type", List.of("application/json"));
      httpHeaders.put("Accept", List.of("application/json"));
    }
    ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestBody, httpHeaders), parameterizedTypeReference);
    return responseEntity;
  }
}
