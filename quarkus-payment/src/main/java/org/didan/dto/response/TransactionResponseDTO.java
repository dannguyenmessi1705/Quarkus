package org.didan.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponseDTO {
  private Long requestId;

  private String content;

  private Long transactionId;

  private String senderAccount;

  private String receiverAccount;

  private Integer amount;

  private List<Details> details = new ArrayList<>();

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  @Builder
  public static class Details {
    private Long transactionId;
    private String accountNumber;
    private String content;
    private Integer amount;
  }

}
