package org.didan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponseDTO {
  private String id;

  private String accountNumber;

  private String name;

  private Double balance;
}
