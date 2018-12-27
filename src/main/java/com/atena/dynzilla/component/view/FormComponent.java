package com.atena.dynzilla.component.view;

import com.atena.dynzilla.*;
import com.atena.dynzilla.core.BeanHelper;
import com.atena.dynzilla.core.DescriptorHelper;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class FormComponent extends AbstractDYNService implements DYNContentService {
  public static final String VALUES_SEPARATOR = "|";
  private static final String PRESELECTED_PROPERTY_KEY = "preselected";
  private static final String SELECTION_VALUES_PROPERTY_KEY = "values";
  private static final String SELECTION_LABEL_PROPERTY_KEY = "labels";
  private List<FormField> fields;
  private Map<String, FormField> fieldMap;
  public FormComponent(String id, DYNModelManager mgr, Map descr) throws DYNException {
    super(id, mgr, descr);
    List fieldElements = DescriptorHelper.getList(descr, "fields", false, this);
    fields = new ArrayList(fieldElements.size());
    fieldMap = new HashMap(fieldElements.size());
    for (Iterator iter = fieldElements.iterator(); iter.hasNext(); ) {
      FormField field = new FormField((Map) iter.next(), this);
      fieldMap.put(field.id, field);
      fields.add(field);
    }
    fieldMap = MapUtils.unmodifiableMap(fieldMap);
    fields = ListUtils.unmodifiableList(fields);
  }

  @Override
  public Object computeOutputParamValue(
      String outputParamName, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    partialUpdate(outputParamName, formRequest, formResult);
    FormField field = fieldMap.get(outputParamName);
    if (field == null) {
      return null;
    }
    return BeanHelper.getBeanProperty(
        formResult, outputParamName + "." + PRESELECTED_PROPERTY_KEY, this);
  }

  @Override
  public void execute(DYNOperationRequest formRequest, DYNOperationResult formResult) {
    try {
      for (FormField field : fields)
          if (shouldFieldUpdate(field, formRequest, formResult)) {
              if (FormField.TYPE.FIELD.equals(field.fieldType)) {
                  updateField(field, formRequest, formResult);
              } else if (FormField.TYPE.SELECTION.equals(field.fieldType)) {
                  updateSelectionField(field, formRequest, formResult);
                  updateMultiSelectionValueLabel(field, formRequest, formResult);
              } else if (FormField.TYPE.MULTI_SELECTION.equals(field.fieldType)) {
                  updateMultiSelectionField(field, formRequest, formResult);
                  updateMultiSelectionValueLabel(field, formRequest, formResult);
              }
          }
    } catch (DYNException ex) {
      logError("Error compute execute form refresh", ex);
      formResult.fail().setException(ex);
    }
  }

  public void partialUpdate(
      String outputParamName, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    FormField field = fieldMap.get(outputParamName);
    if (field != null) {
      if (FormField.TYPE.FIELD.equals(field.fieldType)) {
        updateField(field, formRequest, formResult);
      } else if (FormField.TYPE.SELECTION.equals(field.fieldType)) {
        updateSelectionField(field, formRequest, formResult);
      } else if (FormField.TYPE.MULTI_SELECTION.equals(field.fieldType)) {
        updateMultiSelectionField(field, formRequest, formResult);
      }
    }
  }

  private void updateField(
      FormField field, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    Map fieldResult = MapUtils.getMap(formResult, field.id);
    String preselectedValue = null;
    if (fieldResult != null) {
      preselectedValue = MapUtils.getString(fieldResult, PRESELECTED_PROPERTY_KEY);
    } else {
      formResult.put(field.id, fieldResult = new HashMap(1));
    }
    if (StringUtils.isBlank(preselectedValue) && !StringUtils.isBlank(field.value)) {
      preselectedValue = field.value;
    }
    fieldResult.put(PRESELECTED_PROPERTY_KEY, preselectedValue);
  }

  private void updateSelectionField(
      FormField field, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    Map fieldResult = MapUtils.getMap(formResult, field.id);
    String preselectedValue = null;
    if (fieldResult != null) {
      preselectedValue = MapUtils.getString(fieldResult, PRESELECTED_PROPERTY_KEY);
    } else {
      formResult.put(field.id, fieldResult = new HashMap(3));
    }
    if (StringUtils.isBlank(preselectedValue) && !StringUtils.isBlank(field.value)) {
      preselectedValue = field.value;
    }
    fieldResult.put(PRESELECTED_PROPERTY_KEY, preselectedValue);
  }

  private void updateMultiSelectionField(
      FormField field, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    Map fieldResult = MapUtils.getMap(formResult, field.id);
    String[] preselectedValue = ArrayUtils.EMPTY_STRING_ARRAY;
    if (fieldResult != null) {
      preselectedValue =
          BeanHelper.asStringArray(
              BeanHelper.getBeanProperty(fieldResult, PRESELECTED_PROPERTY_KEY, this));
    } else {
      formResult.put(field.id, fieldResult = new HashMap(3));
    }

    if (ArrayUtils.isEmpty(preselectedValue) && !StringUtils.isBlank(field.value)) {
      preselectedValue = StringUtils.splitPreserveAllTokens(field.value, VALUES_SEPARATOR);
    }
    fieldResult.put(PRESELECTED_PROPERTY_KEY, preselectedValue);
  }

  protected void updateMultiSelectionValueLabel(
      FormField field, DYNOperationRequest formRequest, DYNOperationResult formResult)
      throws DYNException {
    boolean refreshValues = true;
    boolean refreshLabels = true;
    String[][] allSlotsValues = new String[field.slots.size()][];
    int numValueEntries = 0;
    for (int i = 0; i < field.slots.size(); i++) {
      FormField.Slot slot = field.slots.get(i);
      allSlotsValues[i] =
          BeanHelper.asStringArray(
              BeanHelper.getBeanProperty(formResult, field.id + "." + slot.id, this));
      if (ArrayUtils.isEmpty(allSlotsValues[i]) && !StringUtils.isBlank(slot.value)) {
        allSlotsValues[i] = StringUtils.splitPreserveAllTokens(slot.value, VALUES_SEPARATOR);
      }
      numValueEntries = Math.max(numValueEntries, allSlotsValues[i].length);
    }
    /* expands static slots */
    if (numValueEntries > 1) {
      for (int i = 0; i < field.slots.size(); i++) {
        if (allSlotsValues[i].length == 1) {
          String value = allSlotsValues[i][0];
          allSlotsValues[i] = new String[numValueEntries];
          for (int j = 0; j < numValueEntries; j++) {
            allSlotsValues[i][j] = value;
          }
        }
      }
    }

    String[] newValues = new String[numValueEntries];
    String[] newLabels = new String[numValueEntries];
    for (int i = 0; i < numValueEntries; i++) {
      StringBuffer outputBuffer = new StringBuffer();
      StringBuffer labelBuffer = new StringBuffer();
      for (int j = 0; j < field.slots.size(); j++) {
        FormField.Slot slot = field.slots.get(j);
        String value = (i < allSlotsValues[j].length) ? allSlotsValues[j][i] : "";
        if (slot.output) {
          outputBuffer.append(StringUtils.defaultString(value));
        }
        if (slot.label) {
          labelBuffer.append(StringUtils.defaultString(value));
        }
      }
      newValues[i] = outputBuffer.toString();
      newLabels[i] = labelBuffer.toString();
    }

    if (shouldFormUpdate(formRequest, formResult)) {
      refreshLabels = refreshValues = true;
    }

    Map fieldResult = MapUtils.getMap(formResult, field.id);
    String[] oldValues = BeanHelper.asStringArray(fieldResult.get(SELECTION_VALUES_PROPERTY_KEY));
    if (refreshValues) {
      fieldResult.put(SELECTION_VALUES_PROPERTY_KEY, newValues);
    }

    if (refreshLabels) {
      fieldResult.put(SELECTION_LABEL_PROPERTY_KEY, newLabels);
    }
  }

  private boolean shouldFormUpdate(DYNOperationRequest formRequest, DYNOperationResult formResult) {
    // @todo Implement state for when form should update
    return true;
  }

  private boolean shouldFieldUpdate(
      FormField field, DYNOperationRequest formRequest, DYNOperationResult formResult) {

    // @todo Implement state for when field should update
    return true;
  }
}
