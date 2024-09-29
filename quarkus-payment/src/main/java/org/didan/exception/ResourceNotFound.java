package org.didan.exception;

import java.io.Serializable;

public class ResourceNotFound extends RuntimeException implements Serializable {
  public ResourceNotFound(String msg) {
    super(msg);
  }

  public ResourceNotFound(String format, Object... objects) {
    super(String.format(format, objects));
  }
}
