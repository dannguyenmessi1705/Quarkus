package com.didan.spring_payment.controller;

import com.didan.spring_payment.dto.request.AccountRequestDTO;
import com.didan.spring_payment.dto.request.TransferRequestDTO;
import com.didan.spring_payment.dto.response.AccountResponseDTO;
import com.didan.spring_payment.dto.response.GeneralResponse;
import com.didan.spring_payment.dto.response.TransactionResponseDTO;
import com.didan.spring_payment.entity.PaymentEntity;
import com.didan.spring_payment.service.TransactionService;
import com.didan.spring_payment.service.IRestTemplateService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
    MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

  private final TransactionService transactionService;
  private final IRestTemplateService restTemplateService;

  @GetMapping("/query/all")
  public ResponseEntity<GeneralResponse<List<PaymentEntity>>> getAll() {
//    ResponseEntity<GeneralResponse<List<PaymentEntity>>> res = restTemplateService.processParameterizedType(
//        HttpMethod.GET,
//        "http://localhost:8080/payment/query/all",
//        null,
//        null,
//        new ParameterizedTypeReference<GeneralResponse<List<PaymentEntity>>>() {
//        });
//    return res;
    GeneralResponse<List<PaymentEntity>> response = new GeneralResponse<>();
    List<PaymentEntity> payments = transactionService.getAll();
    response.getSuccess(payments);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/transfer")
  public ResponseEntity<GeneralResponse<TransactionResponseDTO>> transfer(@Valid @RequestBody TransferRequestDTO requestDTO) {
    log.info("Transfer request: {}", requestDTO);
    GeneralResponse<TransactionResponseDTO> response = new GeneralResponse<>();
    TransactionResponseDTO responseDTO = transactionService.transfer(requestDTO);
    response.setData(responseDTO);
    response.setMessage("Completed action");
    response.setStatusCode(200);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/balance")
  public ResponseEntity<GeneralResponse<AccountResponseDTO>> balance(@Valid @RequestBody AccountRequestDTO requestDTO) {
    GeneralResponse<AccountResponseDTO> response = transactionService.getBalance(requestDTO);
    return ResponseEntity.ok(response);
  }
}
