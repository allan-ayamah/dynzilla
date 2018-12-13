package com.atena.dynzilla;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

public interface DynManager {

    void start();

    void stop();

    void startModelManager(String modelId) throws  DynException;

    void stopModelManager(String modelId) throws  DynException;

    ModelManager getModelManager(String modelId) throws  DynException;

    InputStream getInputStream(String relativePath) throws IOException;
}
