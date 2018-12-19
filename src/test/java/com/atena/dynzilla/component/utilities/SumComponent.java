package com.atena.dynzilla.component.utilities;

import com.atena.dynzilla.*;

import java.util.Map;

public class SumComponent extends AbstractDYNService implements DYNOperationService {

  public SumComponent(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
  }

  @Override
  public void execute(DYNOperationRequest request, DYNOperationResult result) {
    Map paramMap = request.getParameters();
    try {
      int firstOperandValue = (int) paramMap.get(getId() + ".firstOperand");
      int secondOperandValue = (int) paramMap.get(getId() + ".secondOperand");
      result.put("result", firstOperandValue + secondOperandValue);
      result.setResultCode(DYNConstants.SUCCESS_CODE);
    } catch (Throwable e) {
      result.setResultCode(DYNConstants.ERROR_CODE);
      result.setException(e);
    }
  }
}
