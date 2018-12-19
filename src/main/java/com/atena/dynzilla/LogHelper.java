package com.atena.dynzilla;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    public static Logger getLogger(final Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
