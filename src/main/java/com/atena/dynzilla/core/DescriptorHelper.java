package com.atena.dynzilla.core;

import com.atena.dynzilla.DynException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;

public class DescriptorHelper {

  public static JsonObject getAsJsonObject(JsonObject descr, String propertyName, boolean required)
      throws DynException {
    JsonObject property = descr.getAsJsonObject(propertyName);
    if (required) {
      if (property == null || property.isJsonNull()) {
        throw new DynException("Element[" + propertyName + "] is required");
      }
    }
    return property;
  }

  public static String asString(JsonObject descr, String propertyName, boolean required)
      throws DynException {
    String stringProperty = descr.get(propertyName).getAsString();
    if (required && StringUtils.isBlank(stringProperty)) {
      throw new DynException("Element[" + propertyName + "] \n" + " is required");
    }
    return stringProperty;
  }
}
