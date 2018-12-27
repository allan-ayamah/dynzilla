package com.atena.dynzilla;

import java.util.Map;

public interface DYNOperationResult extends Map {
  static String SUCCESS_CODE = "success";
  static String ERROR_CODE = "error";

  void setResultCode(String resultCode);

  String getResultCode();

  DYNOperationResult fail();

  DYNOperationResult success();

  void setException(Throwable e);
  Throwable getException();

  void setCause(String cause);

    void setOutputCollectorId(String componentId);
    String getOutputCollectorId();
}
