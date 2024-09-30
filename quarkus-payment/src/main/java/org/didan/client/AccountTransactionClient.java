package org.didan.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.didan.dto.request.AccountRequestDTO;
import org.didan.dto.request.TransactionRequestDTO;
import org.didan.dto.response.*;
import org.didan.dto.response.GeneralResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@Path("/transaction")
@RegisterRestClient(configKey = "transaction-api")
public interface AccountTransactionClient {
  @GET
  @Path("/test")
  @Produces(MediaType.APPLICATION_JSON)
  GeneralResponse<AccountResponseDTO> test();

  @POST
  @Path("/transfer")
  @Produces(MediaType.APPLICATION_JSON)
  GeneralResponse<Boolean> transfer(TransactionRequestDTO requestDto);

  @POST
  @Path("/balance")
  @Produces(MediaType.APPLICATION_JSON)
  public GeneralResponse<AccountResponseDTO> balance(AccountRequestDTO requestDto);
}
