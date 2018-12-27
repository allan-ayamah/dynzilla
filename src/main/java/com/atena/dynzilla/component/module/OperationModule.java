package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.ContextFactory;
import com.atena.dynzilla.core.DescriptorHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

public class OperationModule extends AbstractDYNService implements DYNOperationService {
  private String inputParamCollectorId;
  private Set<String> OKCollectorIds;
  private Set<String> KOCollectorIds;

  public OperationModule(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    inputParamCollectorId = DescriptorHelper.getString(descr, "inputParamCollectorId", true, this);
    OKCollectorIds = DescriptorHelper.getSet(descr, "OKParamCollectors", false, this);
    KOCollectorIds = DescriptorHelper.getSet(descr, "KOCollectorIds", false, this);
  }

  @Override
  public void execute(DYNOperationRequest request, DYNOperationResult result) {
    try {
      prepareInitialContext(request);

      doExecute(request, result);
    } catch (Throwable ex) {
      result.setResultCode(DYNConstants.ERROR_CODE);
      result.setException(ex);
    }
  }

  private void doExecute(DYNOperationRequest operationRequest, DYNOperationResult operationResult)
      throws DYNException {
    DYNModelManager mgr = getManager();
    String componentId = inputParamCollectorId;
    while (!StringUtils.isBlank(componentId)) {
      DYNOperationService component = mgr.getOperationService(componentId);
      DYNServiceContextHelper componentHelper = mgr.getServiceCtxHelper(componentId);

      componentHelper.beforeComponentExecutes(operationRequest);
      DYNOperationRequest componentRequest =
          ContextFactory.createNewRequestFrom(
              operationRequest, operationRequest.getServiceParams(componentId));
      DYNOperationResult componentResult = ContextFactory.createNewResult();

      logDebug("========== Executing operation [" + componentId + "] ==========");
      long t1 = System.currentTimeMillis();
      component.execute(componentRequest, componentResult);
      if (component.getLog().isInfoEnabled()) {
        long dt = System.currentTimeMillis() - t1;
        component.logInfo("Execution done in " + dt + " ms");
      }
      // componentHelper.afterComponentExecutes(componentResult, operationRequest);
      String resultCode = null;
      boolean reachedOutput = false;
      if (OKCollectorIds.contains(componentId)) {
        resultCode = DYNOperationResult.SUCCESS_CODE;
        if (OKCollectorIds.size() > 1) {
          resultCode += "." + componentId;
        }
        reachedOutput = true;
      } else if (KOCollectorIds.contains(componentId)) {
        resultCode = DYNOperationResult.ERROR_CODE;
        if (KOCollectorIds.size() > 1) {
          resultCode += "." + componentId;
        }
      } else {
        resultCode = componentResult.getResultCode();
        logDebug("Update context after executing component: '" + componentId + "' ");
        componentHelper.afterComponentExecutes(componentResult, operationRequest);
      }

      if (reachedOutput) {
        logDebug(
            "Reached output collector: '" + componentId + "', resultCode: '" + resultCode + "' ");
        operationResult.putAll(componentResult);
        operationResult.setResultCode(resultCode);
        operationResult.setOutputCollectorId(componentId);
        return;
      }

      // choose next component for execution
      String nextLinkId = componentHelper.getPrimaryOutgoingLink(resultCode);
      if (StringUtils.isBlank(nextLinkId) || !mgr.serviceExists(nextLinkId)) {
        operationResult.putAll(componentResult);
        return;
      }
      DYNLinkService nextLinkService = mgr.getLinkService(nextLinkId);
      componentId = nextLinkService.getTargetId();
    }
  }

  private void prepareInitialContext(DYNOperationRequest request) {
    request.getServiceParams(inputParamCollectorId).putAll(request.getParameters());
  }
}
