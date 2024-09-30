package org.didan.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.didan.client.AccountTransactionClient;
import org.didan.config.KafkaProducer;
import org.didan.dto.request.AccountRequestDTO;
import org.didan.dto.request.TransactionRequestDTO;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.dto.response.AccountResponseDTO;
import org.didan.dto.response.GeneralResponse;
import org.didan.dto.response.TransactionResponseDTO;
import org.didan.dto.response.TransactionResponseDTO.Details;
import org.didan.entity.PaymentEntity;
import org.didan.entity.TransactionEntity;
import org.didan.exception.ResourceNotFound;
import org.didan.repository.PaymentRepository;
import org.didan.repository.TransactionRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class TransactionService {

  @Inject
  PaymentRepository paymentRepository;

  @Inject
  TransactionRepository transactionRepository;

  @Inject
  @RestClient
  AccountTransactionClient accountTransactionClient;

  @Inject
  KafkaProducer kafkaProducer;


  public List<PaymentEntity> getAll() {
    log.info("Find all transaction");
    List<PaymentEntity> transactions = paymentRepository.findAll().stream().toList();
    if (transactions.isEmpty()) {
      log.info("No transaction found");
      throw new ResourceNotFound("No transactions found");
    }
    return transactions;
  }

  @Transactional
  public TransactionResponseDTO transfer(TransferRequestDTO requestDTO) {
    if (requestDTO.getSenderAccount().equals(requestDTO.getReceiverAccount())) {
      log.info("Sender and receiver account number is the same");
      throw new ResourceNotFound("Sender and receiver account number is the same");
    }
    PaymentEntity paymentEntity = new PaymentEntity();
    paymentEntity.setAmount(Double.parseDouble(requestDTO.getAmount().toString()));
    paymentEntity.setSenderAccount(requestDTO.getSenderAccount());
    paymentEntity.setReceiverAccount(requestDTO.getReceiverAccount());
    paymentEntity.setContent(requestDTO.getContent());
    paymentEntity.setStatus("PENDING");
    paymentRepository.persist(paymentEntity);
    // Request Deduction Sender
    TransactionRequestDTO requestSender = transactionConvertApi(requestDTO, paymentEntity, 1);
    TransactionResponseDTO responseDTO = transactionResponseConvertApi(paymentEntity, requestSender);
    responseDTO.setDetails(new ArrayList<>());
    GeneralResponse<Boolean> isSenderDeducted = accountTransactionClient.transfer(requestSender);
    if (isSenderDeducted.getData()) {
      paymentEntity.setStatus("DEDUCTION_SENDER");
      paymentRepository.persist(paymentEntity);
      responseDTO.getDetails().add(new Details(requestSender.getTransactionId(), requestSender.getSenderAccount(), "DEDUCTION_SENDER", responseDTO.getAmount()));
      // Request Add Receiver
      TransactionRequestDTO requestReceiver = transactionConvertApi(requestDTO, paymentEntity, 2);
      GeneralResponse<Boolean> isReceiverAdded = accountTransactionClient.transfer(requestReceiver);
      if (isReceiverAdded.getData()) {
        log.info("Transfer success");
        paymentEntity.setStatus("SUCCESS");
        paymentRepository.persist(paymentEntity);
        responseDTO.getDetails().add(new Details(requestReceiver.getTransactionId(), requestReceiver.getReceiverAccount(), "ADD_RECEIVER", responseDTO.getAmount()));
      } else {
        paymentEntity.setStatus("FAILED");
        responseDTO.getDetails().add(new Details(requestReceiver.getTransactionId(), requestReceiver.getReceiverAccount(), "ADD_RECEIVER_FAILED", responseDTO.getAmount()));
        paymentRepository.persist(paymentEntity);
        log.error("Add receiver failed, prepare to revert sender");
        TransactionRequestDTO revertSenderBalance = transactionConvertApi(requestDTO, paymentEntity,
            3);
        responseDTO.getDetails().add(new Details(revertSenderBalance.getTransactionId(), revertSenderBalance.getSenderAccount(), "REVERT_SENDER", responseDTO.getAmount()));
        kafkaProducer.sendToKafka(revertSenderBalance);
      }
    } else {
      paymentEntity.setStatus("FAILED");
      paymentRepository.persist(paymentEntity);
      log.error("Deduction sender failed");
      responseDTO.getDetails().add(new Details(requestSender.getTransactionId(), requestSender.getSenderAccount(), "DEDUCTION_SENDER_FAILED", responseDTO.getAmount()));
    }
    return responseDTO;
  }

  private TransactionRequestDTO transactionConvertApi(TransferRequestDTO requestDTO,
      PaymentEntity paymentEntity, int typeTransaction) {
    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setPayment(paymentEntity);
    transactionEntity.setTransactionTime(LocalDateTime.now());
    transactionRepository.persist(transactionEntity);
    return TransactionRequestDTO.builder()
        .requestId(paymentEntity.getId())
        .transactionId(transactionEntity.getTransactionId())
        .amount(requestDTO.getAmount())
        .senderAccount(requestDTO.getSenderAccount())
        .receiverAccount(requestDTO.getReceiverAccount())
        .content(requestDTO.getContent())
        .type(typeTransaction)
        .pinCode(requestDTO.getPinCode())
        .build();
  }

  private TransactionResponseDTO transactionResponseConvertApi(PaymentEntity paymentEntity, TransactionRequestDTO requestDTO) {
    return TransactionResponseDTO.builder()
        .requestId(paymentEntity.getId())
        .transactionId(requestDTO.getTransactionId())
        .senderAccount(paymentEntity.getSenderAccount())
        .receiverAccount(paymentEntity.getReceiverAccount())
        .amount((int) Math.round(paymentEntity.getAmount()))
        .content(paymentEntity.getContent())
        .build();
  }

  public GeneralResponse<AccountResponseDTO> getBalance(AccountRequestDTO accountRequestDTO) {
    log.info("Get balance with account number: {}", accountRequestDTO.getAccountNumber());
    return accountTransactionClient.balance(accountRequestDTO);
  }

}
