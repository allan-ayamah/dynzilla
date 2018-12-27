package com.atena.dynzilla;

public interface DYNModelManager {
  DYNService getService(String id) throws DYNException;

  boolean serviceExists(String id) throws DYNException;

  DYNOperationService getOperationService(String id) throws DYNException;

  DYNModelService getModelService() throws DYNException;

  DYNServiceContextHelper getServiceCtxHelper(String serviceId) throws DYNException;

  DYNLinkService getLinkService(String id) throws DYNException;

  DYNContentService getContentService(String sourceId) throws DYNException;
}
