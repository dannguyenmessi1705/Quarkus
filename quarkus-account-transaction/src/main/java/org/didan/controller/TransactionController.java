package org.didan.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.didan.dto.request.AccountRequestDTO;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.dto.response.AccountResponseDto;
import org.didan.dto.response.GeneralResponse;
import org.didan.service.AccountService;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
@Path("/transaction")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

  @Inject
  AccountService accountService;

  @POST
  @Path("/new")
  public GeneralResponse<AccountResponseDto> createAccount(AccountRequestDTO requestDto) {
    log.info("Create account with account number: {}", requestDto.getAccountNumber());
    AccountResponseDto responseDto = accountService.createAccount(requestDto);
    return new GeneralResponse<>(Status.CREATED.getStatusCode(), "Create account successfully",
        responseDto);
  }

  @POST
  @Path("/transfer")
  public GeneralResponse<Boolean> transfer(TransferRequestDTO requestDto) {
    log.info("Transfer request: {}", requestDto);
    accountService.verifyAccount(requestDto);
    GeneralResponse<Boolean> response = new GeneralResponse<>();
    try {
      boolean isTransfer = accountService.transfer(requestDto);
      if (!isTransfer) {
        response.setStatusCode(Status.BAD_REQUEST.getStatusCode());
        response.getSuccess(false);
        response.setMessage("Transfer failed");
      } else {
        response.setStatusCode(Status.OK.getStatusCode());
        response.getSuccess(true);
        response.setMessage("Transfer successfully");
      }
      return response;
    } catch (Exception e) {
      response.setStatusCode(Status.BAD_REQUEST.getStatusCode());
      response.getSuccess(false);
      response.setMessage("Transfer failed");
      return response;
    }
  }

  @POST
  @Path("/balance")
  public GeneralResponse<AccountResponseDto> balance(AccountRequestDTO requestDto) {
    log.info("Get balance with account number: {}", requestDto.getAccountNumber());
    GeneralResponse<AccountResponseDto> response = new GeneralResponse<>();
    response.setMessage("Completed action");
    try {
      log.info("Balance request: {}", requestDto);
      AccountResponseDto responseDto = accountService.getBalance(requestDto);
      response.setData(responseDto);
      response.setStatusCode(Status.OK.getStatusCode());
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage());
      response.setStatusCode(Status.BAD_REQUEST.getStatusCode());
      response.setMessage("Get balance failed");
      response.setData(null);
    }
    return response;
  }

  @GET
  @Path("/test")
  public GeneralResponse<AccountResponseDto> test() {
    AccountResponseDto accountResponseDto = new AccountResponseDto("1234", "523535", "dan", 454.1);
    return new GeneralResponse<>(200, "Success", accountResponseDto);
  }
}
