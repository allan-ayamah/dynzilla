package com.atena.dynzilla;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

public abstract class AbstractService implements Service {
  private String id;
  private JsonObject descr;
  private ModelManager mgr;
  private Logger logger;

  public AbstractService(String id, ModelManager mgr, JsonObject descr) throws DynException {
    this.id = id;
    this.mgr = mgr;
    this.descr = descr;
    this.logger = LogManager.getLogger(this.getClass());
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getName() {
    return null;
  }

  public JsonObject getDescr() {
    return this.descr;
  }

  public ModelManager getManager() {
    return this.mgr;
  }

  public Logger getLog() {
    return this.logger;
  }

  @Override
  public void logInfo(String msg) {
    if (getLog().isInfoEnabled()) {
      getLog().info(getId() + " " + msg);
    }
  }

  @Override
  public void logTrace(String msg) {
    if (getLog().isTraceEnabled()) {
      getLog().trace(getId() + " " + msg);
    }
  }

  @Override
  public void logError(String msg, Throwable e) {
    if (getLog().isErrorEnabled()) {
      getLog().error(getId() + " " + msg, e);
    }
  }

  @Override
  public void logDebug(String msg, Throwable e) {
    if (getLog().isDebugEnabled()) {
      getLog().debug(getId() + " " + msg, e);
    }
  }
}
