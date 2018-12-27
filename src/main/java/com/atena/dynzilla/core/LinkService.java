package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LinkService extends AbstractDYNService implements DYNLinkService {
    private String sourceId;
  private String targetId;
  private String pageSource;
  private List<LinkPropagation> propagations;

  public LinkService(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    sourceId = DescriptorHelper.getString(descr, "sourceId", true, this);
    targetId = DescriptorHelper.getString(descr, "targetId", true, this);
    pageSource = DescriptorHelper.getString(descr, "pageSource", false, this);

    List propagationsDescr = DescriptorHelper.getList(descr, "propagations", false, this);
    List<LinkPropagation> propagations = new ArrayList(propagationsDescr.size());
    for (Iterator iter =  propagationsDescr.iterator(); iter.hasNext();) {
      LinkPropagation linkPropagation = LinkPropagation.create((Map)iter.next(), this);
      propagations.add(linkPropagation);
    }
    this.propagations = ListUtils.unmodifiableList(propagations);
  }

  @Override
  public void propagateBeforeExecute(Map targetParams, DYNOperationRequest operationCtxRequest) throws DYNException {
    for (LinkPropagation propagation : propagations) {
      propagation.propagateBeforeExecute(targetParams, operationCtxRequest);
    }
  }

  @Override
  public void propagateFromResult(Map resultMap, Map targetParams, DYNOperationRequest operationCtxRequest) throws DYNException {
    for (LinkPropagation propagation : propagations) {
      propagation.propagate(resultMap, targetParams);
  }
  }

  @Override
  public void propagateValue(Object value, Map targetParams, DYNOperationRequest opRequest) throws DYNException {
    for (LinkPropagation propagation : propagations) {
      propagation.propagateValue(value, targetParams);
    }
  }

  @Override
  public void propagatePageParams(DYNOperationRequest pageRequest) throws DYNException {
    for(LinkPropagation propagation : propagations) {
      propagation.propagatePageParams(pageRequest);
    }
  }

  @Override
  public String getSourceId() {
    return sourceId;
  }

  @Override
  public String getTargetId() {
    return targetId;
  }
}
