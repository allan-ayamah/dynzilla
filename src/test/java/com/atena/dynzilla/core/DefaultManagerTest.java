package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DefaultManagerTest {
  private static Logger LOG = LogManager.getLogger(DefaultManagerTest.class);
  private DynManager mgr;

  @Before
  public void setUp() {
    String projectPath = SystemUtils.USER_DIR;
    String WEB_INF_DIR = System.getProperty("WEB_INF_DIR");
    ResourceLocator resourceLocator = new ResourceLocator() {

      private String webInfFolder = WEB_INF_DIR;
      @Override
      public InputStream getInputStream(String relativePath) throws IOException {
        InputStream in = null;
        try {
          in = new FileInputStream(webInfFolder+relativePath);
        } catch (FileNotFoundException e) {
          throw new IOException("Could not find resource at "+ relativePath , e);
        }
        return in;
      }
    };
    String descrFolder = "descr/";
    this.mgr = new DefaultManager(descrFolder, resourceLocator);
  }

  @Test
  public void getModelManager() throws DynException {
      ModelManager modelManager = mgr.getModelManager("mdl1");
    assertNotNull(modelManager);
    assertEquals(modelManager, mgr.getModelManager("mdl1"));
  }

  @Test
  public void getInputStream() {}
}
