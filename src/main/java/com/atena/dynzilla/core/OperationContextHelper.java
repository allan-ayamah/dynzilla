package com.atena.dynzilla.core;

import com.atena.dynzilla.*;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.SetUtils;

import java.util.*;

public class OperationContextHelper extends AbstractDYNService implements DYNServiceContextHelper {

  // List tha holds all preceding links
  // they are links coming from components the are not executed
  // like the 'Get session/ set session' components
  private List<String> precedingIncomingLinks;

  private Map<String, List<String>> allOutgoingLinks;

  private Map<String, String> primaryOutgoingLinks;

  private Set<String> outgoingDataLinks;

  public OperationContextHelper(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);

    precedingIncomingLinks = DescriptorHelper.getList(descr, "precedingIncomingLinks", false, this);
    precedingIncomingLinks = ListUtils.unmodifiableList(precedingIncomingLinks);
    List outgoingLinksObj = DescriptorHelper.getList(descr, "outgoingLinks", false, this);
    this.allOutgoingLinks = new HashMap();
    this.primaryOutgoingLinks = new HashMap();
    this.outgoingDataLinks = new HashSet();
    for (Iterator iter = outgoingLinksObj.iterator(); iter.hasNext(); ) {
      Map outLinkObj = (Map) iter.next();
      String linkId = (String) outLinkObj.get("id");
      String resultCode = (String) outLinkObj.get(DYNConstants.RESULT_CODE_PROPERTY);
      boolean isDataLink = MapUtils.getBooleanValue(outLinkObj, "data");

      List<String> resultCodeLinkIds = allOutgoingLinks.get(resultCode);
      if (resultCodeLinkIds == null) {
        resultCodeLinkIds = new ArrayList();
        allOutgoingLinks.put(resultCode, resultCodeLinkIds);
      }
      resultCodeLinkIds.add(linkId);

      if (!isDataLink) {
        primaryOutgoingLinks.put(resultCode, linkId);
      } else {
        outgoingDataLinks.add(linkId);
      }
    }
    allOutgoingLinks = MapUtils.unmodifiableMap(allOutgoingLinks);
    primaryOutgoingLinks = MapUtils.unmodifiableMap(primaryOutgoingLinks);
    outgoingDataLinks = SetUtils.unmodifiableSet(outgoingDataLinks);
  }

  public Map /*<String, List<String>>*/ getAllOutgoingLinks() {
    return allOutgoingLinks;
  }

  public List /*<String>*/ getOutgoinglink(String resultCode) {
    return allOutgoingLinks.get(resultCode);
  }

  public String getPrimaryOutgoingLink(String resultCode) {
    return primaryOutgoingLinks.get(resultCode);
  }

  @Override
  public String getContainterId() {
    return null;
  }

  @Override
  public void beforeComponentExecutes(DYNOperationRequest operationCtxRequest) throws DYNException {

    if (precedingIncomingLinks != null && !precedingIncomingLinks.isEmpty()) {
      DYNModelManager mgr = getManager();
      for(String link : precedingIncomingLinks) {
        DYNLinkService linkService = mgr.getLinkService(link);
        Map targetInputs = operationCtxRequest.getServiceParams(linkService.getTargetId());
        linkService.propagateBeforeExecute(targetInputs, operationCtxRequest);
      }
    }
  }

  @Override
  public void afterComponentExecutes(
      DYNOperationResult result, DYNOperationRequest operationCtxRequest) throws DYNException {
    String resultCode = result.getResultCode();
    if (resultCode == null) {
      throw new DYNException("Result code property cannot be null");
    }

    // String primaryLink = primaryOutgoingLinks.get(resultCode);
    List<String> resultOutGoingLinks = allOutgoingLinks.get(resultCode);
    if (resultOutGoingLinks != null) {
      DYNModelManager mgr = getManager();
      for (String outLinkId : resultOutGoingLinks) {
        DYNLinkService linkService = mgr.getLinkService(outLinkId);
        Map targetInput = operationCtxRequest.getServiceParams(linkService.getTargetId());
        linkService.propagateFromResult(result, targetInput, operationCtxRequest);
      }
    }
  }
}
