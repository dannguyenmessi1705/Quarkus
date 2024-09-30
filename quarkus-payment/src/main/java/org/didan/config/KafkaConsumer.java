package org.didan.config;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.didan.dto.request.TransactionRequestDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
public class KafkaConsumer {
  @Incoming("payment-in")
  @Blocking
  public void receiveFromKafka(TransactionRequestDTO requestDto) {
    log.info("Received message from Kafka: {}", requestDto);
  }

}
