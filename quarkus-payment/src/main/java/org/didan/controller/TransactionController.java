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
import org.didan.dto.request.AccountRequestDto;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.dto.response.GeneralResponse;
import org.didan.entity.TransactionEntity;
import org.didan.service.TransactionService;

@Slf4j
@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionController {

  @Inject
  TransactionService transactionService;

  @GET
  @Path("/query/all")
  public Response getAll() {
    GeneralResponse<List<TransactionEntity>> response = new GeneralResponse<>();
    List<TransactionEntity> transactions = transactionService.getAll();
    response.getSuccess(transactions);
    return Response.ok(response).build();
  }

  @POST
  @Path("/transfer")
  public Response transfer(@Valid TransferRequestDTO requestDTO) {
    log.info("Transfer request: {}", requestDTO);
    return Response.ok().build();
  }

  @POST
  @Path("/balance")
  public Response balance(@Valid AccountRequestDto requestDTO) {
    log.info("Balance request: {}", requestDTO);
    return Response.ok().build();
  }

  @POST
  @Path("/info")
  public Response info(@Valid AccountRequestDto requestDTO) {
    log.info("Info request: {}", requestDTO);
    return Response.ok().build();
  }

}
