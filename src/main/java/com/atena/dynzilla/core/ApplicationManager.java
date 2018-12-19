package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNAppManager;
import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNModelManager;
import com.atena.dynzilla.ResourceLocator;
import com.google.gson.Gson;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ApplicationManager implements DYNAppManager {
  private static Logger LOG = LogManager.getLogger(ApplicationManager.class);
  private static final Class[] MODEL_MANAGER_PARAM_TYPE =
      new Class[] {String.class, DYNAppManager.class, Map.class};

  private String descrFolder;
  private String webInfFolder;
  private ResourceLocator resourceLocator;
  private Map /*<String, DYNModelManager>*/ modelManagersMap;

  public ApplicationManager(
      String webInfFolder, String descrFolder, final ResourceLocator resourceLocator) {
    this.webInfFolder = webInfFolder;
    this.descrFolder = descrFolder;
    this.resourceLocator = resourceLocator;
    this.modelManagersMap = new HashMap();
  }

  public void start() {}

  public void stop() {}

  public void startModelManager(String modelId) throws DYNException {}

  public void stopModelManager(String modelId) throws DYNException {}

  public DYNModelManager getModelManager(String modelId) throws DYNException {
    synchronized (modelManagersMap) {
      DYNModelManager modelManager = (DYNModelManager) modelManagersMap.get(modelId);
      if (modelManager == null) {
        modelManager = getModelMangerFromDescriptor(modelId);
        modelManagersMap.put(modelId, modelManager);
      }
      return modelManager;
    }
  }

  private DYNModelManager getModelMangerFromDescriptor(String modelId) throws DYNException {
    String modelRelativePath = modelId + "/_dyn_model";
    Map descriptor = readDescriptor(modelRelativePath);

    Map managerDescr = (Map) descriptor.get("manager");
    String managerClassName = MapUtils.getString(managerDescr, "service");
    if (StringUtils.isBlank(managerClassName)) {
      throw new DYNException("Model[" + modelId + "] manager class name cannot be empty");
    }

    try {
      Class clazz = Class.forName(managerClassName);
      Constructor constructor = clazz.getConstructor(MODEL_MANAGER_PARAM_TYPE);
      final DYNModelManager modelManager =
          (DYNModelManager) constructor.newInstance(new Object[] {modelId, this, descriptor});
      return modelManager;
    } catch (ClassNotFoundException e) {
      LOG.error("Could not create model manager: " + modelId, e);
      throw new DYNException("Could not create model manager: " + modelId, e);
    } catch (Exception ei) {
      LOG.error("Could not instantiate class for name: " + managerClassName, ei);
      throw new DYNException("Could not instantiate class for name: " + managerClassName, ei);
    }
  }

  private Map readDescriptor(String id) throws DYNException {
    String relativePath =
        StringUtils.replace(descrFolder + id + ".json", "/", SystemUtils.FILE_SEPARATOR);
    LOG.debug("Read descriptor from: " + relativePath);
    System.out.println("Read descriptor from: " + relativePath);
    InputStream in = null;
    try {
      in = getInputStream(relativePath);
    } catch (IOException e2) {
      LOG.error("Could not read stream: " + relativePath, e2);
      IOUtils.closeQuietly(in);
      throw new DYNException("Could not read stream: " + relativePath, e2);
    }

    // read descriptor
    Map descriptor = null;
    try {
      String content = IOUtils.toString(in, Charset.defaultCharset());
      Gson gson = new Gson();
      descriptor = (Map) gson.fromJson(content, Map.class);
      return descriptor;
    } catch (IOException e) {
      LOG.error("Could not read descriptor" + id, e);
      throw new DYNException("Could not read descriptor" + id, e);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  public InputStream getInputStream(String relativePath) throws IOException {
    return resourceLocator.getInputStream(relativePath);
  }
}
