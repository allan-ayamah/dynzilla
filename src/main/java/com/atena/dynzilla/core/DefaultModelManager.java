package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DefaultModelManager implements ModelManager {
  private static final Class[] SERVICE_PARAM_TYPE =
      new Class[] {String.class, ModelManager.class, JsonObject.class};
  private static Logger LOG = LogManager.getLogger(DefaultModelManager.class);

  private String modelId;
  private JsonObject serviceDescriptors;
  private String descrFolder;
  private DynModelService modelService;
  private DynManager dynMgr;
  private Map /*<String, Service>*/ serviceMap;

  public DefaultModelManager(String modelId, DynManager dynMgr, JsonObject modelDescr)
      throws DynException {
    this.modelId = modelId;
    this.serviceDescriptors = DescriptorHelper.getAsJsonObject(modelDescr, "descriptors", true);
    this.dynMgr = dynMgr;
    this.serviceMap = new HashMap();

    this.modelService = (DynModelService) initialiseService(modelId, modelDescr);
  }

  public DynModelService getModelService() throws DynException {
    return this.modelService;
  }

  public Service getService(String id) throws DynException {
    synchronized (serviceMap) {
      Service service = (Service) serviceMap.get(id);
      if (service == null) {
        service = getServiceFromDescr(id);
        serviceMap.put(id, service);
      }
      return service;
    }
  }

  private JsonObject readDescriptor(String id) throws DynException {
    JsonObject descr = this.serviceDescriptors.get(id).getAsJsonObject();
    return descr;
  }

  private Service getServiceFromDescr(String id) throws DynException {
    JsonObject descr = readDescriptor(id);
    if (descr == null) {
      throw new NullPointerException("Component descriptor not found");
    }
    return initialiseService(id, descr);
  }

  private Service initialiseService(String id, JsonObject descr) throws DynException {
    String serviceClassName = DescriptorHelper.asString(descr, "service", true);
    try {
      Class clazz = Class.forName(serviceClassName);
      Constructor constructor = clazz.getConstructor(SERVICE_PARAM_TYPE);
      final Service service = (Service) constructor.newInstance(new Object[] {id, this, descr});
      return service;
    } catch (ClassNotFoundException e) {
      LOG.error("Could not create service: " + id, e);
      throw new DynException("Could not create service: " + id, e);
    } catch (Exception ei) {
      LOG.error("Could not instantiate class for name: " + serviceClassName, ei);
      throw new DynException("Could not instantiate class for name: " + serviceClassName, ei);
    }
  }
}
