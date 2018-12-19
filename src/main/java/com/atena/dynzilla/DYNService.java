package com.atena.dynzilla;

import org.apache.logging.log4j.Logger;

import java.util.Map;

public interface DYNService {
    String getId();

    Map getDescr();

    DYNModelManager getManager();

    Logger getLog();

    void logInfo(String msg);

    void logTrace(String msg);

    void logError(String msg, Throwable e);

    void logDebug(String msg);

    void logDebug(String msg, Throwable e);
}
