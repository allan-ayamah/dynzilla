package com.atena.dynzilla.core;

import com.atena.dynzilla.AbstractService;
import com.atena.dynzilla.DynException;
import com.atena.dynzilla.ModelManager;
import com.google.gson.JsonObject;

public class DefaultPageService extends AbstractService {
    public DefaultPageService(String id, ModelManager mgr, JsonObject descr) throws DynException {
        super(id, mgr, descr);
    }
}
