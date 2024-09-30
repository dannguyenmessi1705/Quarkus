package org.didan.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.didan.client.AccountTransactionClient;
import org.didan.config.KafkaProducer;
import org.didan.dto.request.AccountRequestDTO;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.dto.response.AccountResponseDTO;
import org.didan.dto.response.GeneralResponse;
import org.didan.dto.response.TransactionResponseDTO;
import org.didan.entity.PaymentEntity;
import org.didan.service.TransactionService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

  @Inject
  TransactionService transactionService;

  @Inject
  @RestClient
  AccountTransactionClient accountTransactionClient;

  @Inject
  KafkaProducer kafkaProducer;

  @GET
  @Path("/query/all")
  public GeneralResponse<List<PaymentEntity>> getAll() {
    GeneralResponse<List<PaymentEntity>> response = new GeneralResponse<>();
    List<PaymentEntity> transactions = transactionService.getAll();
    response.getSuccess(transactions);
    return response;
  }

  @POST
  @Path("/transfer")
  public GeneralResponse<TransactionResponseDTO> transfer(@Valid TransferRequestDTO requestDTO) {
    log.info("Transfer request: {}", requestDTO);
    GeneralResponse<TransactionResponseDTO> response = new GeneralResponse<>();
    TransactionResponseDTO responseDTO = transactionService.transfer(requestDTO);
    response.setData(responseDTO);
    response.setMessage("Completed action");
    response.setStatusCode(200);
    return response;
  }

  @POST
  @Path("/balance")
  public GeneralResponse<AccountResponseDTO> balance(@Valid AccountRequestDTO requestDTO) {
    GeneralResponse<AccountResponseDTO> response = transactionService.getBalance(requestDTO);
    return response;
  }

//  @POST
//  @Path("/info")
//  public Response info(@Valid AccountRequestDTO requestDTO) {
//    log.info("Info request: {}", requestDTO);
//    return Response.ok().build();
//  }

//  @GET
//  @Path("/test")
//  public GeneralResponse<AccountResponseDTO> test() {
//    TransactionRequestDTO transactionRequestDto = TransactionRequestDTO.builder()
//        .transactionId(Long.parseLong("123"))
//        .amount(1000)
//        .senderAccount("123")
//        .receiverAccount("456")
//        .content("Test")
//        .pinCode("123")
//        .type(1)
//        .requestId(Long.parseLong("123"))
//        .build();
//    kafkaProducer.sendToKafka(transactionRequestDto);
//    return accountTransactionClient.test();
//  }

//  @Channel("payment")
//  Emitter<String> emitter;
//  @GET
//  @Path("/kafka")
//  public String kafka() {
//    emitter.send("Di Dan");
//    return "Di Dan";
//  }

}
