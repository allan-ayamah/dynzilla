package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public abstract class LinkPropagation {
  protected String targetParam;
  protected DYNLinkService linkService;

  LinkPropagation(String targetParam, DYNLinkService linkService) throws DYNException {
    this.targetParam = targetParam;
    this.linkService = linkService;
  }

  public static LinkPropagation create(Map descr, DYNLinkService linkService) throws DYNException {
    String targetParam = DescriptorHelper.getString(descr, "targetParam", true, linkService);
    String sourceParam = (String) descr.get("sourceParam");
    String strConstantValue =
        StringUtils.defaultIfEmpty((String) descr.get("constantSourceValue"), null);
    String sourceSessionParam =
        DescriptorHelper.getString(descr, "sourceSessionParam", false, linkService);

    if (strConstantValue != null) {
      return new ConstantValuePropagation(strConstantValue, targetParam, linkService);
    }
    return new ComponentLinkPropagation(sourceParam, targetParam, linkService);
  }

  protected void updateTargetInputParams(Object value, Map targetInputParams) {
    if (value != null) {
      targetInputParams.put(targetParam, value);
    }
  }

  public abstract void propagate(Map operationContext, Map targetContext);

  public void propagateValue(Object value, Map targetContext) {
    targetContext.put(targetParam, value);
  }

  public abstract void propagateBeforeExecute(
      Map targetParams, DYNOperationRequest operationCtxRequest);

  public abstract void propagatePageParams(DYNOperationRequest requestContext) throws DYNException;

  public static class ComponentLinkPropagation extends LinkPropagation {
    private String sourceParam;

    public ComponentLinkPropagation(
        String sourceParam, String targetParam, DYNLinkService linkService) throws DYNException {
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
      updateTargetInputParams(value, targetContext);

      linkService.logDebug(
          "Propagate value from sourceParam["
              + sourceParam
              + "] to targetParam["
              + targetParam
              + "] value: "
              + value);
    }

    @Override
    public void propagatePageParams(DYNOperationRequest pageRequest) throws DYNException {
      String sourceId = linkService.getSourceId();
      String targetId = linkService.getTargetId();
      DYNOperationRequest trgRequest = pageRequest.getServiceRequest(targetId);
      DYNOperationResult srcResultBean = pageRequest.getServiceResultBean(sourceId);

      Object srcParamValue = BeanHelper.getBeanProperty(srcResultBean, sourceParam, linkService);
      if (srcParamValue != null) {
        updateTargetInputParams(srcParamValue, trgRequest.getParameters());
        return;
      }

      DYNContentService contentService = linkService.getManager().getContentService(sourceId);
      try {
        srcParamValue =
            contentService.computeOutputParamValue(
                sourceParam,
                pageRequest.getServiceRequest(sourceId),
                pageRequest.getServiceResultBean(sourceId));
        updateTargetInputParams(srcParamValue, trgRequest.getParameters());
      } catch (Throwable ex) {
        linkService.logError(
            "Error compute propagate [componentId: "
                + sourceId
                + ", sourceParam: "
                + sourceParam
                + ", targetParam: "
                + targetParam
                + "]",
            ex);
        throw new DYNException(ex);
      }
    }
  }

  public static class ConstantValuePropagation extends LinkPropagation {
    private String constantValue;

    public ConstantValuePropagation(
        String constantValue, String targetParam, DYNLinkService linkService) throws DYNException {
      super(targetParam, linkService);
      this.constantValue = constantValue;
    }

    @Override
    public void propagatePageParams(DYNOperationRequest pageRequest) throws DYNException {
      String targetId = linkService.getTargetId();
      DYNOperationRequest trgRequest = pageRequest.getServiceRequest(targetId);
      String srcParamValue = constantValue;
      updateTargetInputParams(srcParamValue, trgRequest.getParameters());
      linkService.logDebug(
          "Propagate constant value to [targetParam:"
              + targetParam
              + ", value: "
              + srcParamValue
              + "]");
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
      updateTargetInputParams(value, targetContext);
      linkService.logDebug(
          "Propagate constant value to targetParam[" + targetParam + "] value: " + value);
    }
  }
}
