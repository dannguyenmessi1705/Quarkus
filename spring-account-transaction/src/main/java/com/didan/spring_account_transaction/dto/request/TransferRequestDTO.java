package com.didan.spring_account_transaction.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequestDTO {
  @NotBlank(message = "Request id is mandatory")
  private String requestId;

  @NotBlank(message = "Pin code is mandatory")
  private String pinCode;

  @NotBlank(message = "Transaction id is mandatory")
  private String transactionId;

  @NotBlank(message = "Account number is mandatory")
  private String senderAccount;

  @NotBlank(message = "Account number is mandatory")
  private String receiverAccount;

  @NotNull(message = "Amount is mandatory")
  @Positive(message = "Amount must be positive")
  private Integer amount;

  @NotNull(message = "Type is mandatory")
  private Integer type;

  private String content;
}
