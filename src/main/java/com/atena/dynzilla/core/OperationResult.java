package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNConstants;
import com.atena.dynzilla.DYNOperationResult;

import java.util.HashMap;

public class OperationResult extends HashMap implements DYNOperationResult {
  public OperationResult() {}

  public OperationResult(boolean result) {
    if (result) {
      setResultCode(DYNConstants.SUCCESS_CODE);
    } else {
      setResultCode(DYNConstants.ERROR_CODE);
    }
  }

  @Override
  public DYNOperationResult fail() {
    setResultCode(ERROR_CODE);
    return this;
  }

  @Override
  public DYNOperationResult success() {
    setResultCode(SUCCESS_CODE);
    return this;
  }

  @Override
  public void setResultCode(String resultCode) {
    put("resultCode", resultCode);
  }

  @Override
  public String getResultCode() {
    return (String) get("resultCode");
  }

  @Override
  public void setException(Throwable e) {
    put("resultException", e);
  }

  @Override
  public Throwable getException() {
    Object ex = get("resultException");
    if (ex != null && ex instanceof Throwable) {
      return (Throwable) ex;
    }
    return null;
  }

  @Override
  public void setCause(String cause) {
    put("cause", cause);
  }

  @Override
  public void setOutputCollectorId(String collectorId) {
    put("outputCollectorId", collectorId);
  }

  @Override
  public String getOutputCollectorId() {
    return (String) get("outputCollectorId");
  }
}
