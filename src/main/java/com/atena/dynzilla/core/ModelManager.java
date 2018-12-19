package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ModelManager implements DYNModelManager {
  private static final Class[] SERVICE_PARAM_TYPE =
      new Class[] {String.class, DYNModelManager.class, Map.class};
  private static Logger LOG = LogManager.getLogger(ModelManager.class);

  private String modelId;
  private Map /*<String, Object>*/ serviceDescriptors;
  private String descrFolder;
  private DYNModelService modelService;
  private DYNAppManager dynMgr;
  private final Map<String, DYNService> serviceMap;
  /* cache for service tha exists */
  private final Map<String, Boolean> serviceExistsMap;
  private Map<String, String> serviceCtxHelperIdClassNameMap;

  public ModelManager(String modelId, DYNAppManager dynMgr, Map modelDescr) throws DYNException {
    LOG.info("Model[" + modelId + "] manager init ");
    this.modelId = modelId;
    this.dynMgr = dynMgr;
    Map managerDescr = MapUtils.getMap(modelDescr, "manager");
    Map serviceIdClassNameMap = MapUtils.getMap(managerDescr, "serviceContextHelpers");
    if (serviceIdClassNameMap == null) {
      LOG.error("Model[" + modelId + "] missing element \"contextServices\"");
      throw new DYNException("Model[" + modelId + "] missing element \"serviceContextHelpers\"");
    }
    this.serviceCtxHelperIdClassNameMap = MapUtils.unmodifiableMap(serviceIdClassNameMap);
    this.serviceDescriptors = MapUtils.unmodifiableMap(MapUtils.getMap(modelDescr, "descriptors"));
    this.serviceMap = new HashMap();
    this.serviceExistsMap = new HashMap();
    this.modelService = (DYNModelService) initialiseService(modelId, modelDescr);
  }

  public DYNModelService getModelService() throws DYNException {
    return this.modelService;
  }

  @Override
  public boolean serviceExists(String id) throws DYNException {
    synchronized (serviceMap) {
      if(serviceMap.get(id) != null) {
        return true;
      }
      synchronized (serviceExistsMap) {
        if(Boolean.TRUE.equals(serviceExistsMap.get(id))) {
          return true;
        }
        Map serviceDescr = readDescriptor(id);
        boolean exists = (serviceDescr != null);
        serviceExistsMap.put(id, exists);
        return exists;
    }
    }
  }

  @Override
  public DYNService getService(String id) throws DYNException {
    synchronized (serviceMap) {
      DYNService service = (DYNService) serviceMap.get(id);
      if (service == null) {
        service = getServiceFromDescr(id);
        serviceMap.put(id, service);
      }
      return service;
    }
  }

  @Override
  public DYNServiceContextHelper getServiceCtxHelper(String serviceId) throws DYNException {
    String serviceParamId = serviceId + DYNServiceContextHelper.ID_SUFFIX;
    return (DYNServiceContextHelper) getService(serviceParamId);
  }

  @Override
  public DYNOperationService getOperationService(String id) throws DYNException {
    DYNService service = getService(id);
    if (service instanceof DYNOperationService) {
      return (DYNOperationService) service;
    }
    throw new DYNException("Service[" + id + "] does not implement class: " + DYNOperationService.class);
  }

  @Override
  public DYNLinkService getLinkService(String id) throws DYNException {
    DYNService service = getService(id);
    if (service instanceof DYNLinkService) {
      return (DYNLinkService) service;
  }
    throw new DYNException("Service[" + id + "] does not implement class: " + DYNLinkService.class);
  }

  private Map readDescriptor(String id) throws DYNException {
    String actualServiceId = id;
    boolean isServiceParamHelper = StringUtils.endsWith(id, DYNServiceContextHelper.ID_SUFFIX);
    if (isServiceParamHelper) {
      actualServiceId = StringUtils.removeEnd(id, DYNServiceContextHelper.ID_SUFFIX);
    }
    Map actualServiceDescr = MapUtils.getMap(this.serviceDescriptors, actualServiceId);
    if (actualServiceDescr == null) {
      return null;
    }
    if (isServiceParamHelper) {
      actualServiceDescr =
          MapUtils.getMap(actualServiceDescr, DYNServiceContextHelper.DESCRIPTOR_PROPERTY_NAME);
      if(actualServiceDescr == null) {
        return null;
      }
      String serviceCtxId = MapUtils.getString(actualServiceDescr, "context");
      actualServiceDescr.put("service", this.serviceCtxHelperIdClassNameMap.get(serviceCtxId));
    }
    return actualServiceDescr;
  }

  private DYNService getServiceFromDescr(String id) throws DYNException {
    Map descr = readDescriptor(id);
    if (descr == null) {
      throw new NullPointerException("Component descriptor not found");
    }
    return initialiseService(id, descr);
  }

  private DYNService initialiseService(String id, Map descr) throws DYNException {
    String serviceClassName = MapUtils.getString(descr, "service");
    if (StringUtils.isBlank(serviceClassName)) {
      DYNException ex =
          new DYNException(
              "DYNService[" + id + "] class name not found for model [" + this.modelId + "]");
    }
    try {
      Class clazz = Class.forName(serviceClassName);
      Constructor constructor = clazz.getConstructor(SERVICE_PARAM_TYPE);
      final DYNService service =
          (DYNService) constructor.newInstance(new Object[] {id, this, descr});
      return service;
    } catch (ClassNotFoundException e) {
      LOG.error("Could not create service: " + id, e);
      throw new DYNException("Could not create service: " + id, e);
    } catch (Exception ei) {
      LOG.error("Could not instantiate class for name: " + serviceClassName, ei);
      throw new DYNException("Could not instantiate class for name: " + serviceClassName, ei);
    }
  }
}
