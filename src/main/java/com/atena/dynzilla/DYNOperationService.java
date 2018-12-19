package com.atena.dynzilla;

public interface DYNOperationService extends DYNService {

  void execute(DYNOperationRequest request, DYNOperationResult result);
}
