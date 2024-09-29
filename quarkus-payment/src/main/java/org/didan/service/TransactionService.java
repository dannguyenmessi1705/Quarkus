package org.didan.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.didan.entity.TransactionEntity;
import org.didan.exception.ResourceNotFound;
import org.didan.repository.TransactionRepository;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class TransactionService {
  @Inject
  TransactionRepository transactionRepository;

  public List<TransactionEntity> getAll() {
    log.info("Find all transaction");
    List<TransactionEntity> transactions = transactionRepository.findAll().stream().toList();
    if (transactions.isEmpty()) {
      log.info("No transaction found");
      throw new ResourceNotFound("No transactions found");
    }
    return transactions;
  }

}
