package org.didan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRequestDTO {
  private Long requestId;

  private Long transactionId;

  private String senderAccount;

  private String receiverAccount;

  private Integer amount;

  private String content;

  private Integer type;

  private String pinCode;
}
