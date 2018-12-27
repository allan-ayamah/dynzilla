package com.atena.dynzilla.component.view;

import com.atena.dynzilla.DYNContentService;
import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.core.DescriptorHelper;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FormField {

  public static enum TYPE {
    FIELD,
    SELECTION,
    MULTI_SELECTION,
  }

  public String id;
  public TYPE fieldType;
  public String dataType;
  public String value;
  public boolean preloaded;
  public List<Slot> slots;

  public FormField(Map descr, DYNContentService formBasedService) throws DYNException {
    String id = DescriptorHelper.getString(descr, "id", true, formBasedService);
    String fieldType = DescriptorHelper.getString(descr, "fieldType", true, formBasedService);
    if (fieldType == "Field") {
      this.fieldType = TYPE.FIELD;
    } else if (fieldType == "SelectionField") {
      this.fieldType = TYPE.SELECTION;
    } else if (fieldType == "MultiSelection") {
      this.fieldType = TYPE.MULTI_SELECTION;
    } else {
      formBasedService.logError("Invalid field type '" + fieldType + "", null);
      throw new DYNException("Invalid field type '" + fieldType + "");
    }
    this.id = id;
    this.dataType = DescriptorHelper.getString(descr, "dataType", true, formBasedService);
    this.value =
        StringUtils.defaultIfEmpty(
            DescriptorHelper.getString(descr, "dataType", false, formBasedService), null);
    this.preloaded = MapUtils.getBooleanValue(descr, "preloaded");

    List slotElements = DescriptorHelper.getList(descr, "slots", false, formBasedService);
    List<Slot> slots = new ArrayList(slotElements.size());
    for (Iterator iter = slotElements.iterator(); iter.hasNext(); ) {
      slots.add(new Slot((Map) iter.next()));
    }
    this.slots = ListUtils.unmodifiableList(slots);
  }

  @Override
  public String toString() {
    return "FormField{" +
            "id='" + id + '\'' +
            ", fieldType=" + fieldType +
            ", dataType='" + dataType + '\'' +
            ", value='" + value + '\'' +
            ", preloaded=" + preloaded +
            ", slots=" + slots +
            '}';
  }

  public static class Slot {
    public String id;
    public boolean output;
    public boolean label;
    public String value;

    public Slot(Map slotElement) {
      this.id = MapUtils.getString(slotElement, "id");
      this.output = MapUtils.getBooleanValue(slotElement, "output");
      this.label = MapUtils.getBooleanValue(slotElement, "label");
      this.value = StringUtils.defaultIfEmpty(MapUtils.getString(slotElement, "value"), null);
    }

    @Override
    public String toString() {
      return "Slot{" +
              "id='" + id + '\'' +
              ", output=" + output +
              ", label=" + label +
              ", value='" + value + '\'' +
              '}';
    }
  }
}
