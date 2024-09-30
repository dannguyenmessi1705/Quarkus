package org.didan.config;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.didan.dto.request.TransactionRequestDto;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.service.AccountService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
public class KafkaConsumer {
  @Inject
  AccountService accountService;

  @Incoming("payment-in")
  @Blocking
  public void receiveFromKafka(TransactionRequestDto requestDTO) {
    log.info("Received message 2 from Kafka: {}", requestDTO);
    accountService.revertSenderBalance(requestDTO);
  }
}
