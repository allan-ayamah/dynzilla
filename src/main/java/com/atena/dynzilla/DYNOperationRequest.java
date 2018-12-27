package com.atena.dynzilla;

import java.util.Map;

public interface DYNOperationRequest {
    Map/*<String, Object>*/ getParameters();
    DYNContextInfo getContextInfo();
    DYNSession getSessionContext();

    /* Every component in an operation context has a set on input*/
    Map getServiceParams(String id);
    DYNOperationRequest getServiceRequest(String serviceId);
    DYNOperationResult getServiceResultBean(String serviceId);
}
