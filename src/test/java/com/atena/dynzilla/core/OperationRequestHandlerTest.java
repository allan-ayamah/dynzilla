package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.junit.Test;

import java.util.Map;

public class OperationRequestHandlerTest extends AbstractDYNServiceTest {
  private DYNAppManager appMgr;
  private DYNModelManager modelMgr;

  public OperationRequestHandlerTest() throws DYNException {
    super();
    this.appMgr = new ApplicationManager(getTestWebInfDir(),getTestDescrDir(), getTestResourceLocator());
    this.modelMgr = appMgr.getModelManager("test-operation-chain");
  }

  @Test
  public void executeAndPropagate() throws DYNException {
    logDebug("TEST");

    Map sessionContext = ContextFactory.createNewSessionContext();
    Map operationContext = ContextFactory.createNewLocalContext();

    DYNContextInfo ctxInfo;

/*
    operationContext.put("sumc1.firstOperand", 1);
    operationContext.put("sumc1.secondOperand", 2);

    DYNOperationService sumComp1 = modelMgr.getOperationService("sumc1");
    DYNServiceContextHelper comp1Helper = modelMgr.getServiceCtxHelper(sumComp1.getId());
    Map sumc1Result = sumComp1.execute(operationContext, sessionContext, null);


    comp1Helper.computeNewContextFromResult(sumc1Result, operationContext, ctxInfo);

    DYNModelManager mgr = modelMgr;

    String resultCode = MapUtils.getString(sumc1Result, DYNConstants.RESULT_CODE_PROPERTY);
    String nextTargetId = comp1Helper.getPrimaryOutgoingLink(resultCode);
    DYNOperationService nextOpService = mgr.getOperationService(nextTargetId);
    DYNServiceContextHelper nextServiceHelper = mgr.getServiceCtxHelper(nextTargetId);

    Map nextServiceInputs = ctxInfo.getServiceInput(nextTargetId);
    Map nextResult = nextOpService.execute(nextServiceInputs, session, ctxInfo);
    nextServiceHelper.computeNewContextFromResult(nextResult, sess);*/
}
}
