package com.atena.dynzilla;

public interface ModelManager {
    Service getService(String id) throws DynException;
}
