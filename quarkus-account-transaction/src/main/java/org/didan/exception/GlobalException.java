package org.didan.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.didan.dto.error.ErrorDTO;

@Provider
public class GlobalException implements ExceptionMapper<ResourceNotFound> {

  @Override
  public Response toResponse(ResourceNotFound exception) {
    return Response.status(Status.NOT_FOUND)
        .entity(new ErrorDTO(exception.getMessage(), Status.NOT_FOUND.getStatusCode())).build();
  }
}
