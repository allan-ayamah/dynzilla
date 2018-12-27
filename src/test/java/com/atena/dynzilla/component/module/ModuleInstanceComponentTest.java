package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.ContextFactory;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ModuleInstanceComponentTest extends AbstractDYNServiceTest {

  @Test
  public void execute() throws DYNException {

    DYNModelManager modelMgr = getAppManager().getModelManager("operation-module-test");
    DYNOperationRequest request =
        ContextFactory.createNewOperationRequest(
            new HashMap() {
              {
                put("icc1", 1);
                put("icc2", 1);
            }
            });
    DYNOperationResult result = ContextFactory.createNewResult();
    modelMgr.getOperationService("mic1").execute(request, result);
    assertEquals(DYNOperationResult.SUCCESS_CODE, result.getResultCode());
    assertEquals(2, result.get("occ1"));
    assertEquals(2, result.get("result"));
}

  @Test
  public void normalizeParams() {}
}
