package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNOperationRequest;
import com.atena.dynzilla.DYNOperationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ContextFactory {

  private ContextFactory() {}

  public static DYNOperationRequest createNewOperationRequest(Map params) {
    return new OperationRequest(params);
  }

  public static DYNOperationRequest createNewRequestFrom(DYNOperationRequest request, Map params) {
    return OperationRequest.createFrom(request, params);
  }

  public static DYNOperationResult createNewResult() {
    return (DYNOperationResult) new OperationResult();
  }

  public static DYNOperationRequest createNewResult(boolean successful) {
    return (DYNOperationRequest) new OperationResult(successful);
  }

  /**
   * Creates a new synchronized context map having scope application.
   *
   * @return the application context map
   */
  public static Map createNewApplicationContext() {
    return Collections.synchronizedMap(new HashMap());
  }

  /**
   * Creates a new synchronized context map having scope session.
   *
   * @return the session context map
   */
  public static Map createNewSessionContext() {
    return Collections.synchronizedMap(new HashMap());
  }

  /**
   * Creates a new synchronized context map having scope session.
   *
   * @param initValues the context initialization values
   * @return the session context map
   */
  public static Map createNewSessionContext(Map initValues) {
    return Collections.synchronizedMap(new HashMap(initValues));
  }

  /**
   * Creates a new context map having scope request.
   *
   * @return the local context map
   */
  public static Map createNewLocalContext() {
    return new HashMap();
  }

  /**
   * Creates a new context map having scope request.
   *
   * @param initValues the context initialization values
   * @return the local context map
   */
  public static Map createNewLocalContext(Map initValues) {
    return new HashMap(initValues);
  }
}
