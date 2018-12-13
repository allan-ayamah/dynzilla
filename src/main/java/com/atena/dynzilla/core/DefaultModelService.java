package com.atena.dynzilla.core;

import com.atena.dynzilla.AbstractService;
import com.atena.dynzilla.DynException;
import com.atena.dynzilla.DynModelService;
import com.atena.dynzilla.ModelManager;
import com.google.gson.JsonObject;

public class DefaultModelService extends AbstractService implements DynModelService {

  public DefaultModelService(String id, ModelManager mgr, JsonObject descr) throws DynException {
    super(id, mgr, descr);
    logInfo("Starting service");
  }

}
