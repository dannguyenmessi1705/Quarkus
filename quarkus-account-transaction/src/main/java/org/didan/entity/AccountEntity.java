package org.didan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "transaction")
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "pin_code", nullable = false)
  private String pinCode;

  @Column(name = "balance")
  private Double balance;

}