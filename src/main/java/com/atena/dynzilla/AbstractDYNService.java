package com.atena.dynzilla;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class AbstractDYNService implements DYNService {
  private String id;
  private Map descr;
  private DYNModelManager mgr;
  private Logger logger;

  public AbstractDYNService(String id, DYNModelManager mgr, Map descr) throws DYNException {
    this.id = id;
    this.mgr = mgr;
    this.descr = descr;
    this.logger = LogManager.getLogger(this.getClass());
  }

  @Override
  public String getId() {
    return this.id;
  }


  public Map getDescr() {
    return this.descr;
  }

  public DYNModelManager getManager() {
    return this.mgr;
  }

  public Logger getLog() {
    return this.logger;
  }

  @Override
  public void logInfo(String msg) {
    if (getLog().isInfoEnabled()) {
      getLog().info(getId() + ": " + msg);
    }
  }

  @Override
  public void logTrace(String msg) {
    if (getLog().isTraceEnabled()) {
      getLog().trace(getId() + ": " + msg);
    }
  }

  @Override
  public void logError(String msg, Throwable e) {
    if (getLog().isErrorEnabled()) {
      getLog().error(getId() + ": " + msg, e);
    }
  }


  public void logDebug(String msg) {
    if(getLog().isDebugEnabled()) {
      getLog().debug(getId() + ": " + msg);
    }
  }

  @Override
  public void logDebug(String msg, Throwable e) {
    if (getLog().isDebugEnabled()) {
      getLog().debug(getId() + ": " + msg, e);
    }
  }
}
