package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNOperationRequest;
import com.atena.dynzilla.DYNService;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public abstract class LinkPropagation {
  protected String targetParam;
  protected DYNService linkService;

  LinkPropagation(String targetParam, DYNService linkService) throws DYNException {
    this.targetParam = targetParam;
    this.linkService = linkService;
  }

  public static LinkPropagation create(Map descr, DYNService linkService) throws DYNException {
    String targetParam = DescriptorHelper.getString(descr, "targetParam", true, linkService);
    String sourceParam = (String) descr.get("sourceParam");
    String strConstantValue =
        StringUtils.defaultIfEmpty((String) descr.get("constantSourceValue"), null);
    String sourceSessionParam =
        DescriptorHelper.getString(descr, "sourceSessionParam", false, linkService);
    if (!StringUtils.isBlank(sourceSessionParam)) {
      return new SessionParameterPropagation(sourceSessionParam, targetParam, linkService);
    }
    if (strConstantValue != null) {
      return new ConstantValuePropagation(strConstantValue, targetParam, linkService);
    }
    return new BasicLinkPropagation(sourceParam, targetParam, linkService);
  }

  public abstract void propagate(Map operationContext, Map targetContext);

  public void propagateValue(Object value, Map targetContext) {
    targetContext.put(targetParam, value);
  }

  public abstract void propagateBeforeExecute(
      Map targetParams, DYNOperationRequest operationCtxRequest);

  public static class BasicLinkPropagation extends LinkPropagation {
    private String sourceParam;

    public BasicLinkPropagation(String sourceParam, String targetParam, DYNService linkService)
        throws DYNException {
      super(targetParam, linkService);
      this.sourceParam = sourceParam;
    }

    @Override
    public void propagateBeforeExecute(Map targetParams, DYNOperationRequest operationCtxRequest) {
      /** NOTHING */
    }

    @Override
    public void propagate(Map operationContext, Map targetContext) {
      Object value = operationContext.get(sourceParam);
      targetContext.put(targetParam, value);
      linkService.logDebug(
          "Propagate value from sourceParam["
              + sourceParam
              + "] to targetParam["
              + targetParam
              + "] value: "
              + value);
    }
  }

  public static class ConstantValuePropagation extends LinkPropagation {
    private String constantValue;

    public ConstantValuePropagation(
        String constantValue, String targetParam, DYNService linkService) throws DYNException {
      super(targetParam, linkService);
      this.constantValue = constantValue;
    }

    @Override
    public void propagateBeforeExecute(Map targetParams, DYNOperationRequest operationCtxRequest) {
      Object value = constantValue;
      targetParams.put(targetParam, value);
      linkService.logDebug(
          "PropagateBeforeExecute constant value to targetParam["
              + targetParam
              + "] value: "
              + value);
    }

    @Override
    public void propagate(Map operationContext, Map targetContext) {
      Object value = constantValue;
      targetContext.put(targetParam, value);
      linkService.logDebug(
          "Propagate constant value to targetParam[" + targetParam + "] value: " + value);
    }
  }

  private static class SessionParameterPropagation extends LinkPropagation {
    private String sourceSessionParam;

    public SessionParameterPropagation(
        String sourceSessionParam, String targetParam, DYNService linkService) throws DYNException {
      super(targetParam, linkService);
      this.sourceSessionParam = sourceSessionParam;
    }

    @Override
    public void propagateBeforeExecute(Map targetParams, DYNOperationRequest operationCtxRequest) {
      Object value = operationCtxRequest.getSessionContext().get(sourceSessionParam);
      targetParams.put(targetParam, value);

      linkService.logDebug(
          "PropagateBeforeExecute session param["
              + sourceSessionParam
              + "] to targetParam["
              + targetParam
              + "] value: "
              + value);
    }

    @Override
    public void propagate(Map operationContext, Map targetContext) {
      /* do nothing, session parameters are propagated before execution*/
    }
  }
}
