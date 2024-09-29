package org.didan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "transaction")
public class TransactionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "sender_account", nullable = false)
  private String senderAccount;

  @Column(name = "receiver_account", nullable = false)
  @NotEmpty
  private String receiverAccount;

  @Column(name = "transaction_id", nullable = false)
  @NotEmpty
  private String transactionId;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "content")
  private String content;

  @Column(name = "status")
  private String status;

  @Column(name = "transaction_time")
  private LocalDateTime transactionTime;

}