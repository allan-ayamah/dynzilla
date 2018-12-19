package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ModelManagerTest extends AbstractDYNServiceTest {
  private DYNModelManager modelMgr;

  public ModelManagerTest() throws DYNException {
    super();
    String webInfDir = getTestWebInfDir();
    String descrFolder = getTestDescrDir();
    ResourceLocator resourceLocator = getTestResourceLocator();
    DYNAppManager appManager = new ApplicationManager(webInfDir, descrFolder, resourceLocator);

    String modelId = "test-model-manager";
    modelMgr = appManager.getModelManager(modelId);
  }

  @Before
  public void getModelService() throws DYNException {
    assertNotNull(modelMgr.getModelService());
  }

  @Test
  public void serviceExists() throws DYNException {
    assertEquals(true, modelMgr.serviceExists("opm1"));
    assertEquals(true, modelMgr.serviceExists("sumc1"));

    assertEquals(false, modelMgr.serviceExists("opmooioiopi"));
  }

  @Test
  public void getService() {}

  @Test
  public void getServiceCtxHelper() throws DYNException {
    try {
      modelMgr.getServiceCtxHelper("opm1");
      fail("opm1 context helper should be null");
    } catch (NullPointerException expected) {
    }

    // mic1 is module instance of "opm1"
    // so it should have a context helper instance
    assertNotNull(modelMgr.getService("mic1"));
    assertNotNull(modelMgr.getServiceCtxHelper("mic1"));
  }

  @Test
  public void getOperationService() {}

  @Test
  public void getLinkService() {
    try{
    assertNotNull(modelMgr.getLinkService("ln1"));
    } catch (Throwable e) {
      fail(e.getMessage());
    }
  }
}
