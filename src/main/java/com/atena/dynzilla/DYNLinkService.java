package com.atena.dynzilla;

import java.util.Map;

public interface DYNLinkService extends DYNService{
  String getSourceId();

  String getTargetId();

  void propagateBeforeExecute(Map targetParams, DYNOperationRequest operationCtxRequest)
      throws DYNException;

  void propagateFromResult(Map resultMap, Map targetParams, DYNOperationRequest opRequest)
      throws DYNException;

  void propagateValue(Object value, Map targetParams, DYNOperationRequest opRequest)
      throws DYNException;

  void propagatePageParams(DYNOperationRequest requestContext) throws DYNException;
}
