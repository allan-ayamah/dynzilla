package com.atena.dynzilla;

import java.io.IOException;
import java.io.InputStream;

public interface DYNAppManager {

    void start();

    void stop();

    void startModelManager(String modelId) throws DYNException;

    void stopModelManager(String modelId) throws DYNException;

    DYNModelManager getModelManager(String modelId) throws DYNException;

    InputStream getInputStream(String relativePath) throws IOException;
}
