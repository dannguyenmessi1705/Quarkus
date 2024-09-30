package org.didan.config;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.didan.dto.request.TransactionRequestDto;

public class TransactionRequestDeserializer extends ObjectMapperDeserializer<TransactionRequestDto> {
  public TransactionRequestDeserializer() {
    super(TransactionRequestDto.class);
  }
}
