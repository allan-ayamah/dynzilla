package com.atena.dynzilla;


public class DynException extends Exception {

    public DynException(String msg) {
        super(msg);
    }

    public DynException(String msg, Throwable e) {
        super(msg, e);
    }
}
