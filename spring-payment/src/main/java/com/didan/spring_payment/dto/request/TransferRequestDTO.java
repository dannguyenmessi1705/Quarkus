package com.didan.spring_payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequestDTO {
  @NotBlank(message = "Account number is mandatory")
  private String senderAccount;

  @NotBlank(message = "Account number is mandatory")
  private String receiverAccount;

  @Positive(message = "Amount must be positive")
  private Integer amount;

  private String content;

  @NotBlank(message = "PIN code is mandatory")
  private String pinCode;
}
