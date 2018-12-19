package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNService;
import com.google.gson.Gson;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class DescriptorHelper {

  public static Map getMap(Map descr, String propertyName, boolean required, DYNService service)
      throws DYNException {
    Map property = MapUtils.getMap(descr, propertyName);
    if (property == null) {
      if (required) {
        service.logError("Element[" + propertyName + "] \n" + " is required", null);
        throw new DYNException("Element[" + propertyName + "] \n" + " is required");
      }
      return MapUtils.EMPTY_MAP;
    }
    return property;
  }

  public static String getString(
      Map descr, String propertyName, boolean required, DYNService service) throws DYNException {
    String stringProperty = MapUtils.getString(descr, propertyName);
    if (required && StringUtils.isBlank(stringProperty)) {
      service.logError("Element[" + propertyName + "] \n" + " is required", null);
      throw new DYNException("Element[" + propertyName + "] \n" + " is required");
    }
    return stringProperty;
  }

  public static List getList(Map descr, String propName, boolean required, DYNService service)
      throws DYNException {
    Object arrayProp = descr.get(propName);
    if (arrayProp == null) {
      if (required) {
        service.logError("Property[" + propName + "] is missing", null);
        throw new DYNException("Property[" + propName + "] is missing");
      }
      return Collections.EMPTY_LIST;
    }
    return (List) arrayProp;
  }

  public static Set getSet(Map descr, String propName, boolean required, DYNService service)
      throws DYNException {
    return new HashSet(getList(descr, propName, required, service));
  }

  public static Map fromJson(String jsonDescr) {
    Gson gson = new Gson();
    return gson.fromJson(jsonDescr, Map.class);
  }
}
