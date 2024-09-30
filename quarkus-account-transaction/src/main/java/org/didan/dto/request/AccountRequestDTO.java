package org.didan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotEmpty(message = "Balance is mandatory")
  private Double balance;
}
