package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.ApplicationManager;
import com.atena.dynzilla.core.ContextFactory;
import com.atena.dynzilla.core.DescriptorHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OperationModuleTest extends AbstractDYNServiceTest {
  private DYNModelManager modelMgr;

  public OperationModuleTest() throws DYNException {
    super();
    String webInfDir = getTestWebInfDir();
    String descrFolder = getTestDescrDir();
    ResourceLocator resourceLocator = getTestResourceLocator();
    DYNAppManager appManager = new ApplicationManager(webInfDir, descrFolder, resourceLocator);

    String modelId = "operation-module-test";
    modelMgr = appManager.getModelManager(modelId);
  }


  @Test
  public void execute() throws DYNException {
    String opm1 = "opm1";
    Map operationParams = new HashMap() {{
      put("icc1", 1);
      put("icc2", 2);
    }};
    DYNOperationRequest request = ContextFactory.createNewOperationRequest(operationParams);
    DYNOperationResult result = ContextFactory.createNewResult();

    DYNOperationService opModule1 = modelMgr.getOperationService(opm1);
    opModule1.execute(request, result);
    assertEquals(DYNOperationResult.SUCCESS_CODE, result.getResultCode());
    assertNotNull(result.get("occ1"));
    assertEquals(2, result.get("occ1"));
  }

  // @Test
  public void constructorTest() throws DYNException {
    String jsonDescr =
        "{\n"
            + "      \"inputParamCollectorId\": \"ipc1\",\n"
            + "      \"OKParamCollectors\": [\"opc1\", \"opc3\"],\n"
            + "      \"KOParamCollectors\": [\"opc2\"]\n"
            + "    }";
    String id = "opm1";
    DYNModelManager mgr = null;
    Map descr = DescriptorHelper.fromJson(jsonDescr);
    assertEquals("ipc1", DescriptorHelper.getString(descr, "inputParamCollectorId", true, this));
    assertThat(
        Arrays.asList("opc1", "opc3"),
        is(DescriptorHelper.getList(descr, "OKParamCollectors", true, this)));
    assertThat(
        Arrays.asList("opc2"),
        is(DescriptorHelper.getList(descr, "KOParamCollectors", true, this)));
    try {
      DYNOperationService opModule = new OperationModule(id, mgr, descr);
    } catch (DYNException ex) {
      fail(ex.getMessage());
    }
  }
}
