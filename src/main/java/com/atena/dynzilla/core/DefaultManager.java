package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class DefaultManager implements DynManager {
  private static Logger LOG = LogManager.getLogger(DefaultManager.class);
  private static final Class[] MODEL_MANAGER_PARAM_TYPE =
      new Class[] {String.class, DynManager.class, JsonObject.class};

  private String descrFolder;
  private ResourceLocator resourceLocator;
  private Map /*<String, ModelManager>*/ modelManagersMap;

  public DefaultManager(String descrFolder, final ResourceLocator resourceLocator) {
    this.descrFolder = descrFolder;
    this.resourceLocator = resourceLocator;
    this.modelManagersMap = new HashMap();
  }

  public void start() {}

  public void stop() {}

  public void startModelManager(String modelId) throws DynException {}

  public void stopModelManager(String modelId) throws DynException {}

  public ModelManager getModelManager(String modelId) throws DynException {
    synchronized (modelManagersMap) {
      ModelManager modelManager = (ModelManager) modelManagersMap.get(modelId);
      if (modelManager == null) {
        modelManager = getModelMangerFromDescriptor(modelId);
        modelManagersMap.put(modelId, modelManager);
      }
      return modelManager;
    }
  }

  private ModelManager getModelMangerFromDescriptor(String modelId) throws DynException {
    String modelRelativePath = modelId + "/_dyn_model";
    JsonObject descriptor = readDescriptor(modelRelativePath);

    String managerClassName = descriptor.get("manager").getAsString();
    if (StringUtils.isBlank(managerClassName)) {
      throw new DynException("Model manager name cannot be empty");
    }

    try {
      Class clazz = Class.forName(managerClassName);
      Constructor constructor = clazz.getConstructor(MODEL_MANAGER_PARAM_TYPE);
      final ModelManager modelManager =
          (ModelManager) constructor.newInstance(new Object[] {modelId, this, descriptor});
      return modelManager;
    } catch (ClassNotFoundException e) {
      LOG.error("Could not create model manager: " + modelId, e);
      throw new DynException("Could not model manager: " + modelId, e);
    } catch (Exception ei) {
      LOG.error("Could not instantiate class for name: " + managerClassName, ei);
      throw new DynException("Could not instantiate class for name: " + managerClassName, ei);
    }
  }

  private JsonObject readDescriptor(String id) throws DynException {
    String relativePath = descrFolder + id + ".json";
    LOG.debug("Read descriptor from: " + relativePath);
    System.out.println("Read descriptor from: " + relativePath);
    InputStream in = null;
    try {
      in = getInputStream(relativePath);
    } catch (IOException e2) {
      LOG.error("Could not read stream: " + relativePath, e2);
      IOUtils.closeQuietly(in);
      throw new DynException("Could not read stream: " + relativePath, e2);
    }

    // read descriptor
    JsonObject descriptor = null;
    try {
      String content = IOUtils.toString(in, Charset.defaultCharset());
      Gson gson = new Gson();
      descriptor = gson.fromJson(content, JsonObject.class);
      return descriptor;
    } catch (IOException e) {
      LOG.error("Could not read descriptor" + id, e);
      throw new DynException("Could not read descriptor" + id, e);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  public InputStream getInputStream(String relativePath) throws IOException {
    return resourceLocator.getInputStream(relativePath);
  }
}
