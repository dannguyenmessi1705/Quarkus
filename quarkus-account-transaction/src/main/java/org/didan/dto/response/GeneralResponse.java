package org.didan.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeneralResponse <T> {
  private Integer statusCode;
  private String message;
  private T data;

  public void getSuccess(T data) {
    this.setStatusCode(200);
    this.setMessage("Fetch data successfully");
    this.setData(data);
  }

  public void createSuccess(T data) {
    this.setStatusCode(201);
    this.setMessage("Create data successfully");
    this.setData(data);
  }

  public void updateSuccess(T data) {
    this.setStatusCode(204);
    this.setMessage("Update data successfully");
    this.setData(data);
  }

  public void deleteSuccess(T data) {
    this.setStatusCode(204);
    this.setMessage("Delete data successfully");
    this.setData(data);
  }

  public void getFailed() {
    this.setStatusCode(404);
    this.setMessage("Get data failed");
  }

  public void createFailed() {
    this.setStatusCode(409);
    this.setMessage("Create data failed");
  }

  public void updateFailed() {
    this.setStatusCode(409);
    this.setMessage("Update data failed");
  }

  public void deleteFailed() {
    this.setStatusCode(400);
    this.setMessage("Delete data failed");
  }

}
