package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;

import java.util.Map;

public class ModuleInstanceComponent extends AbstractDYNService implements DYNOperationService {
    public ModuleInstanceComponent(String id, DYNModelManager mgr, Map descr) throws DYNException {
        super(id, mgr, descr);
        logDebug("Module instance is up");
    }

    @Override
    public void execute(DYNOperationRequest request, DYNOperationResult result) {

    }
}
