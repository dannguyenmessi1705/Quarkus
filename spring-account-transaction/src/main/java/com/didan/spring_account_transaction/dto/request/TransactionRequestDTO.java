package com.didan.spring_account_transaction.dto.request;

import jakarta.validation.constraints.NotBlank;
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

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class AccountBalanceRequestDTO {
    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotBlank(message = "Pin code is mandatory")
    private String pinCode;
  }
}
