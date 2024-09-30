package org.didan.config;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.didan.dto.request.TransactionRequestDTO;

public class TransactionRequestDeserializer extends ObjectMapperDeserializer<TransactionRequestDTO> {
  public TransactionRequestDeserializer() {
    super(TransactionRequestDTO.class);
  }
}
