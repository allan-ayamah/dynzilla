package com.atena.dynzilla;


public class DYNException extends Exception {

    public DYNException(Throwable e) {
        super(e);
    }

    public DYNException(String msg) {
        super(msg);
    }

    public DYNException(String msg, Throwable e) {
        super(msg, e);
    }
}
