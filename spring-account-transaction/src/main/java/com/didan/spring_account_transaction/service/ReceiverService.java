package com.didan.spring_account_transaction.service;

import com.didan.spring_account_transaction.dto.request.TransactionRequestDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ReceiverService {
  private final AccountService accountService;

  @KafkaListener(topics = "payment", groupId = "payment-in")
  public void consume(TransactionRequestDTO requestDTO) {
    log.info("Receive message from Kafka: {}", requestDTO);
    accountService.revertSenderBalance(requestDTO);
  }
}
