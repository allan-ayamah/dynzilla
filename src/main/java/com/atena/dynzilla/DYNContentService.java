package com.atena.dynzilla;

public interface DYNContentService extends DYNService {
    Object computeOutputParamValue(String outputParamName, DYNOperationRequest opRequest, DYNOperationResult opResult) throws DYNException;
    void execute(DYNOperationRequest request, DYNOperationResult resultBean) throws DYNException;
}
