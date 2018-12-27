package com.atena.dynzilla.component.module;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.DescriptorHelper;
import org.apache.commons.collections.SetUtils;

import java.util.Map;
import java.util.Set;

public class ModuleInstanceComponent extends AbstractDYNService implements DYNOperationService {
  private String moduleId;
  private String moduleType;
  private Set<String> inputParamsIds;

  public ModuleInstanceComponent(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    logDebug("Module instance is up");
    Map moduleElem = DescriptorHelper.getMap(descr, "module", true, this);
    moduleId = DescriptorHelper.getString(moduleElem, "id", true, this);
    moduleType = DescriptorHelper.getString(moduleElem, "type", true, this);
    inputParamsIds =
        SetUtils.unmodifiableSet(DescriptorHelper.getSet(descr, "inputParams", false, this));
  }

  @Override
  public void execute(DYNOperationRequest request, DYNOperationResult result) {
    try {
      Map reqParams = request.getParameters();
      //normalizeInputCollectorParams(reqParams);
      DYNOperationService opModule = getManager().getOperationService(moduleId);
      if (getLog().isInfoEnabled()) {
        logInfo("Execute operation module '" + moduleId + "' ");
      }
      opModule.execute(request, result);
      if (getLog().isInfoEnabled()) {
        logInfo("Finish executing operation module '" + moduleId + "' ");
        logInfo("Result: " + result.getResultCode());
      }
      //normalizeOutputParam(result);
    } catch (Throwable ex) {
      logError("Error occured while running operation module", ex);
      result.setException(ex);
      return;
    }
  }

  protected void normalizeInputCollectorParams(Map paramMap) {
    boolean logParams = getLog().isDebugEnabled();
    String prefix = getId() + ".";
    logDebug(paramMap.toString());
    for (String entKey : inputParamsIds) {
      if (logParams) {
        logInfo("InputParam[id: " + entKey + ", value: " + paramMap.get(entKey) + "]");
      }
      paramMap.put(entKey, paramMap.get(entKey));
    }
  }

  protected void normalizeOutputParam(Map paramMap) {
    if (paramMap == null || paramMap.isEmpty()) return;
    boolean logParams = getLog().isDebugEnabled();
    String prefix = getId() + ".";
    // DON'T iterate the keySet because the map,
    // java will throw an exception
    Object[] entryKeys = paramMap.keySet().toArray();

    for (int i = 0; i < entryKeys.length; i++) {
      Object entKey = entryKeys[i];
      if (logParams) {
        logDebug(
            "OutputParam[id: "
                + prefix
                + entKey.toString()
                + ", value: "
                + paramMap.get(entKey)
                + "]");
      }
      paramMap.put(prefix + entKey.toString(), paramMap.get(entKey));
    }
  }
}
