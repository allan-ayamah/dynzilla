package com.atena.dynzilla;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;
public interface Service {

    String getId();

    String getName();

    JsonObject getDescr();

    ModelManager getManager();

    Logger getLog();

    void logInfo(String msg);

    void logTrace(String msg);

    void logError(String msg, Throwable e);

    void logDebug(String msg, Throwable e);
}
