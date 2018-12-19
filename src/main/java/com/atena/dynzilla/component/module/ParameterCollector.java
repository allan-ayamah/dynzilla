package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.DescriptorHelper;
import org.apache.commons.collections.ListUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParameterCollector extends AbstractDYNService implements DYNOperationService {
  private String type;
  private List parameters;

  public ParameterCollector(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    type = DescriptorHelper.getString(descr, "type", true, this);
    parameters = ListUtils.unmodifiableList(DescriptorHelper.getList(descr, "params", false, this));
  }

  @Override
  public void execute(DYNOperationRequest request, DYNOperationResult result) {
    try {
      Map requestParams = request.getParameters();
      for (Iterator iter = parameters.iterator(); iter.hasNext(); ) {
        Map param = (Map) iter.next();
        Object reqParamValue = requestParams.get(param.get("id"));
        if (reqParamValue == null && param.get("value") != null) {
          reqParamValue = param.get("value");
        }
        result.put(param.get("id"), reqParamValue);
        result.put(param.get("name"), reqParamValue);
        if (getLog().isDebugEnabled()) {
          logDebug("Param[" + param.toString() + "]: " + reqParamValue);
        }
      }
      result.success();
    } catch (Throwable ex) {
      result.fail().setException(ex);
      logError("Error occured while computing parameter values", ex);
    }
  }
}
