package com.didan.spring_payment.config;

import com.didan.spring_payment.exception.GlobalException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
@Data
public class ClientRequest {

  @Bean
  public RestTemplate clientRequestRestTemplate(){
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(5000);
    factory.setConnectionRequestTimeout(5000);
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.setErrorHandler(new GlobalException());
    return restTemplate;
  }

}
