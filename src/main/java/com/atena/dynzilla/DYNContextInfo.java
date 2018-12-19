package com.atena.dynzilla;

import java.util.Map;

public interface DYNContextInfo {

    Map getServiceInput(String id);
    boolean inTransaction();
    boolean inPageContext();
}
