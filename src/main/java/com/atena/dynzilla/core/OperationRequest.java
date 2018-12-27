package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNContextInfo;
import com.atena.dynzilla.DYNOperationRequest;
import com.atena.dynzilla.DYNOperationResult;
import com.atena.dynzilla.DYNSession;

import java.util.HashMap;
import java.util.Map;

public class OperationRequest implements DYNOperationRequest {
    private DYNSession sessionCtx;
    private DYNContextInfo contextInfo;
    private Map paramMap;
    /* serves as */
    private Map/*<String, Map>*/ serviceIdAllocatedParamSpace;

    public OperationRequest(final Map inputParams) {
        this.paramMap = inputParams;
        this.serviceIdAllocatedParamSpace = new HashMap();
    }

    public OperationRequest(final Map inputParams, DYNSession sessionCtx, DYNContextInfo ctxInfo) {
        this(inputParams);
        this.sessionCtx = sessionCtx;
        this.contextInfo = ctxInfo;
    }

    public static OperationRequest createFrom(DYNOperationRequest request, Map params) {
        OperationRequest newRequest = new OperationRequest(params, request.getSessionContext(), request.getContextInfo());
        return newRequest;
    }

    @Override
    public Map getParameters() {
        return paramMap;
    }

    @Override
    public DYNContextInfo getContextInfo() {
        return null;
    }

    @Override
    public DYNSession getSessionContext() {
        return null;
    }

    @Override
    public Map getServiceParams(String id) {
        Map preAllocatedParamSpace = (Map) serviceIdAllocatedParamSpace.get(id);
        if(preAllocatedParamSpace == null) {
            preAllocatedParamSpace = ContextFactory.createNewLocalContext();
            serviceIdAllocatedParamSpace.put(id, preAllocatedParamSpace);
        }
        return preAllocatedParamSpace;
    }

    @Override
    public DYNOperationRequest getServiceRequest(String serviceId) {
        return null;
    }

    @Override
    public DYNOperationResult getServiceResultBean(String serviceId) {
        return null;
    }
}
