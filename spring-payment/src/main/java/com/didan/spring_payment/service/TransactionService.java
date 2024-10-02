package com.didan.spring_payment.service;

import com.didan.spring_payment.dto.request.AccountRequestDTO;
import com.didan.spring_payment.dto.request.TransactionRequestDTO;
import com.didan.spring_payment.dto.request.TransferRequestDTO;
import com.didan.spring_payment.dto.response.AccountResponseDTO;
import com.didan.spring_payment.dto.response.GeneralResponse;
import com.didan.spring_payment.dto.response.TransactionResponseDTO;
import com.didan.spring_payment.dto.response.TransactionResponseDTO.Details;
import com.didan.spring_payment.entity.PaymentEntity;
import com.didan.spring_payment.entity.TransactionEntity;
import com.didan.spring_payment.exception.ResourceNotFound;
import com.didan.spring_payment.repository.PaymentRepository;
import com.didan.spring_payment.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
  @Value("${client.endpoint.account-transaction}")
  private String accountTransactionEndpoint;
  @Value("${client.protocol}")
  private String protocol;

  private final KafkaTemplate<String, TransactionRequestDTO> kafkaTemplate;
  private final PaymentRepository paymentRepository;
  private final TransactionRepository transactionRepository;
  private final IRestTemplateService restTemplateService;

  public List<PaymentEntity> getAll() {
    log.info("Find all transaction");
    // find all payment
    List<PaymentEntity> transactions = paymentRepository.findAll();
    log.info("Found {} transactions", transactions.size());
    if (transactions.isEmpty()) {
      log.info("No transaction found");
      throw new ResourceNotFound("No transactions found");
    }
    return transactions;
  }

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
    paymentRepository.save(paymentEntity);
    // Request Deduction Sender
    TransactionRequestDTO requestSender = transactionConvertApi(requestDTO, paymentEntity, 1);
    TransactionResponseDTO responseDTO = transactionResponseConvertApi(paymentEntity,
        requestSender);
    responseDTO.setDetails(new ArrayList<>());
    ResponseEntity<GeneralResponse<Boolean>> senderResponse = restTemplateService.processParameterizedType(
        HttpMethod.POST,
        protocol + "://" + accountTransactionEndpoint + "/transaction/transfer", null, requestSender,
        new ParameterizedTypeReference<GeneralResponse<Boolean>>() {});
    boolean isSenderDeducted = senderResponse.getBody().getData();
    if (isSenderDeducted) {
      paymentEntity.setStatus("DEDUCTION_SENDER");
      paymentRepository.save(paymentEntity);
      responseDTO.getDetails().add(
          new Details(requestSender.getTransactionId(), requestSender.getSenderAccount(),
              "DEDUCTION_SENDER", responseDTO.getAmount()));
      // Request Add Receiver
      TransactionRequestDTO requestReceiver = transactionConvertApi(requestDTO, paymentEntity, 2);
      ResponseEntity<GeneralResponse<Boolean>> receiveResponse = restTemplateService.processParameterizedType(
          HttpMethod.POST,
          protocol + "://" + accountTransactionEndpoint +"/transaction/transfer", null, requestReceiver,
          new ParameterizedTypeReference<GeneralResponse<Boolean>>() {});
      GeneralResponse<Boolean> isReceiverAdded = receiveResponse.getBody();
      if (isReceiverAdded.getData()) {
        log.info("Transfer success");
        paymentEntity.setStatus("SUCCESS");
        paymentRepository.save(paymentEntity);
        responseDTO.getDetails().add(
            new Details(requestReceiver.getTransactionId(), requestReceiver.getReceiverAccount(),
                "ADD_RECEIVER", responseDTO.getAmount()));
      } else {
        paymentEntity.setStatus("FAILED");
        responseDTO.getDetails().add(
            new Details(requestReceiver.getTransactionId(), requestReceiver.getReceiverAccount(),
                "ADD_RECEIVER_FAILED", responseDTO.getAmount()));
        paymentRepository.save(paymentEntity);
        log.error("Add receiver failed, prepare to revert sender");
        TransactionRequestDTO revertSenderBalance = transactionConvertApi(requestDTO, paymentEntity,
            3);
        responseDTO.getDetails().add(new Details(revertSenderBalance.getTransactionId(),
            revertSenderBalance.getSenderAccount(), "REVERT_SENDER", responseDTO.getAmount()));
        kafkaTemplate.send("payment", revertSenderBalance);
      }
    } else {
      paymentEntity.setStatus("FAILED");
      paymentRepository.save(paymentEntity);
      log.error("Deduction sender failed");
      responseDTO.getDetails().add(
          new Details(requestSender.getTransactionId(), requestSender.getSenderAccount(),
              "DEDUCTION_SENDER_FAILED", responseDTO.getAmount()));
    }
    return responseDTO;
  }

  private TransactionRequestDTO transactionConvertApi(TransferRequestDTO requestDTO,
      PaymentEntity paymentEntity, int typeTransaction) {
    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setPayment(paymentEntity);
    transactionEntity.setTransactionTime(LocalDateTime.now());
    transactionRepository.save(transactionEntity);
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

  private TransactionResponseDTO transactionResponseConvertApi(PaymentEntity paymentEntity,
      TransactionRequestDTO requestDTO) {
    TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
    transactionResponseDTO.setRequestId(paymentEntity.getId());
    transactionResponseDTO.setTransactionId(requestDTO.getTransactionId());
    transactionResponseDTO.setSenderAccount(paymentEntity.getSenderAccount());
    transactionResponseDTO.setReceiverAccount(paymentEntity.getReceiverAccount());
    transactionResponseDTO.setAmount((int) Math.round(paymentEntity.getAmount()));
    transactionResponseDTO.setContent(paymentEntity.getContent());
    return transactionResponseDTO;
  }

  public GeneralResponse<AccountResponseDTO> getBalance(AccountRequestDTO accountRequestDTO) {
    log.info("Get balance with account number: {}", accountRequestDTO.getAccountNumber());
    ResponseEntity<GeneralResponse<AccountResponseDTO>> response = restTemplateService.processParameterizedType(
        HttpMethod.POST,
        protocol + "://" + accountTransactionEndpoint + "/transaction/balance", null, accountRequestDTO,
        new ParameterizedTypeReference<GeneralResponse<AccountResponseDTO>>() {
        });
    return response.getBody();
  }
}
