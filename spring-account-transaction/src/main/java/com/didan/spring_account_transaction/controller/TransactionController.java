package com.didan.spring_account_transaction.controller;

import com.didan.spring_account_transaction.dto.request.AccountBalanceRequestDTO;
import com.didan.spring_account_transaction.dto.request.AccountRequestDTO;
import com.didan.spring_account_transaction.dto.request.TransferRequestDTO;
import com.didan.spring_account_transaction.dto.response.AccountResponseDTO;
import com.didan.spring_account_transaction.dto.response.GeneralResponse;
import com.didan.spring_account_transaction.service.AccountService;
import jakarta.validation.Valid;
import javax.net.ssl.SSLEngineResult.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/transaction", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
    MediaType.APPLICATION_JSON_VALUE})
public class TransactionController {

  private final AccountService accountService;

  @PostMapping("/new")
  public ResponseEntity<GeneralResponse<AccountResponseDTO>> createAccount(
      @Valid @RequestBody AccountRequestDTO requestDTO) {
    log.info("Create account with account number: {}", requestDTO.getAccountNumber());
    AccountResponseDTO responseDTO = accountService.createAccount(requestDTO);
    return new ResponseEntity<>(
        new GeneralResponse<>(HttpStatus.CREATED.value(), "Create account successfully",
            responseDTO), HttpStatus.CREATED);
  }

  @PostMapping("/transfer")
  public ResponseEntity<GeneralResponse<Boolean>> transfer(
      @Valid @RequestBody TransferRequestDTO requestDTO) {
    log.info("Transfer request: {}", requestDTO);
    GeneralResponse<Boolean> response = new GeneralResponse<>();
    try {
      boolean isTransfer = accountService.transfer(requestDTO);
      if (!isTransfer) {
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Transfer failed");
        response.setData(false);
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Transfer successfully");
        response.setData(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
      }
    } catch (Exception e) {
      response.setStatusCode(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Transfer failed");
      response.setData(false);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
  }

  @PostMapping("/balance")
  public ResponseEntity<GeneralResponse<AccountResponseDTO>> balance(
      @Valid @RequestBody AccountBalanceRequestDTO requestDTO) {
    log.info("Get balance with account number: {}", requestDTO.getAccountNumber());
    GeneralResponse<AccountResponseDTO> response = new GeneralResponse<>();
    response.setMessage("Completed action");
    try {
      log.info("Balance request: {}", requestDTO);
      AccountResponseDTO responseDto = accountService.getBalance(requestDTO);
      response.setData(responseDto);
      response.setStatusCode(HttpStatus.OK.value());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage());
      response.setStatusCode(HttpStatus.BAD_REQUEST.value());
      response.setMessage("Get balance failed");
      response.setData(null);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
  }
}
