package com.atena.dynzilla.core;

import com.atena.dynzilla.AbstractDYNService;
import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNModelService;
import com.atena.dynzilla.DYNModelManager;

import java.util.Map;

public class ModelService extends AbstractDYNService implements DYNModelService {
  public ModelService(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    logInfo("Starting model service");
  }

}
