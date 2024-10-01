package com.didan.spring_payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountRequestDTO {
  @NotBlank(message = "Account number is mandatory")
  private String accountNumber;

  @NotBlank(message = "Pin code is mandatory")
  private String pinCode;
}
