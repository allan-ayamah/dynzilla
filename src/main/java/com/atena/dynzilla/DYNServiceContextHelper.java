package com.atena.dynzilla;

/*
   When a component is placed in a context(page or operation) parameter forwarding behave differently
*/
public interface DYNServiceContextHelper {
    String DESCRIPTOR_PROPERTY_NAME = "DYNContext";
    String ID_SUFFIX = ".context";

    // the id of the page or operationModule in which the component is placed
  String getContainterId();

  String getPrimaryOutgoingLink(String resultCode);
    /**
     *  The method is called before the service is executed
     * @param operationCtxRequest - The current operation request(scope = request)
     */
  void beforeComponentExecutes(DYNOperationRequest operationCtxRequest) throws DYNException;

  void afterComponentExecutes(DYNOperationResult result, DYNOperationRequest operationCtxRequest) throws DYNException;

}
