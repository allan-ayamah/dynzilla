package com.atena.dynzilla;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class AbstractDYNServiceTest implements DYNService {
  private Logger log;
  private ResourceLocator resourceLocator;
  final String resourceDir = System.getProperty("RESOURCE_DIR");
  final String webInfRelDir = System.getProperty("WEB_INF_REL_DIR");
  final String descrRelDir = System.getProperty("DESCR_REL_DIR");

  public AbstractDYNServiceTest() {
    log = LogManager.getLogger(getClass());
    this.resourceLocator =
        new ResourceLocator() {

          private String resourceRoot = resourceDir;

          @Override
          public InputStream getInputStream(String relativePath) throws IOException {
              relativePath = StringUtils.replace(relativePath, "/", File.separator);
            InputStream in = null;
            try {
              in = new FileInputStream(resourceRoot + relativePath);
            } catch (FileNotFoundException e) {
              throw new IOException("Could not find resource at " + relativePath, e);
            }
            return in;
          }
        };
  }

  protected JsonObject readDescriptor(String relativePath) throws DYNException {
    log.debug("Read descriptor from: " + relativePath);
    InputStream in = null;
    try {
      in = getInputStream(relativePath);
    } catch (IOException e2) {
      log.error("Could not read stream: " + relativePath, e2);
      IOUtils.closeQuietly(in);
      throw new DYNException("Could not read stream: " + relativePath, e2);
    }

    // read descriptor
    JsonObject descriptor = null;
    try {
      String content = IOUtils.toString(in, Charset.defaultCharset());
      Gson gson = new Gson();
      descriptor = gson.fromJson(content, JsonObject.class);
      return descriptor;
    } catch (IOException e) {
      log.error("Could not read descriptor" + relativePath, e);
      throw new DYNException("Could not read descriptor" + relativePath, e);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  protected Map readDescriptorAsMap(String relativePath) throws DYNException {
    log.debug("Read AS Map descriptor from: " + relativePath);
    InputStream in = null;
    try {
      in = getInputStream(relativePath);
    } catch (IOException e2) {
      log.error("Could not read stream: " + relativePath, e2);
      IOUtils.closeQuietly(in);
      throw new DYNException("Could not read stream: " + relativePath, e2);
    }

    // read descriptor
    Map descriptor = null;
    try {
      String content = IOUtils.toString(in, Charset.defaultCharset());
      Gson gson = new Gson();
      descriptor = (Map)gson.fromJson(content, Map.class);
      return descriptor;
    } catch (IOException e) {
      log.error("Could not read descriptor AS MAP" + relativePath, e);
      throw new DYNException("Could not read descriptor AS MAP" + relativePath, e);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  public ResourceLocator getTestResourceLocator() {
    return resourceLocator;
  }


  public String getTestWebInfDir(){
    return webInfRelDir;
  }

  public String getTestDescrDir() {
    return descrRelDir;
  }



  public InputStream getInputStream(String relativePath) throws IOException {
    return resourceLocator.getInputStream(relativePath);
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public Map getDescr() {
    return null;
  }

  @Override
  public DYNModelManager getManager() {
    return null;
  }

  @Override
  public Logger getLog() {
    return log;
  }

  @Override
  public void logInfo(String msg) {
    log.info(msg);
  }

  @Override
  public void logTrace(String msg) {
    log.trace(msg);
  }

  @Override
  public void logError(String msg, Throwable e) {
    log.error(msg, e);
  }

  @Override
  public void logDebug(String msg) {
    log.debug(msg);
  }

  @Override
  public void logDebug(String msg, Throwable e) {
    log.debug(msg, e);
  }
}
