package org.didan.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.didan.dto.request.TransactionRequestDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
@Slf4j
public class KafkaProducer {
  @Inject
  @Channel("payment")
  Emitter<TransactionRequestDTO> emitter;

  public void sendToKafka(TransactionRequestDTO requestDto) {
    log.info("Send message to Kafka: {}", requestDto);
    emitter.send(requestDto);
  }
}
