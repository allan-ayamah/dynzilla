package com.atena.dynzilla.core;

import com.atena.dynzilla.DYNException;
import com.atena.dynzilla.DYNService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;

public class BeanHelper {

    /**
     * Gets an object array from an object. If the input object is an object array, a clone of it is returned. If the input object is
     * not an object array, a singleton object array is built from it and returned.
     *
     * @param obj
     *            an object from which a object array should be returned.
     * @return the object array built from <code>obj</code>
     */
    public static final Object[] asObjectArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        }
        if (obj instanceof Object[]) {
            int length = ((Object[]) obj).length;
            Object[] result = new Object[length];
            for (int i = 0; i < length; i++) {
                result[i] = ((Object[]) obj)[i];
            }
            return result;
        } else if (obj.getClass().isArray()) {
            Object[] result = new Object[Array.getLength(obj)];
            for (int i = 0; i < result.length; i++) {
                result[i] = Array.get(obj, i);
            }
            return result;
        }
        return new Object[] { obj };
    }


    public static final Object getBeanProperty(Object bean, String propName, DYNService service)
            throws DYNException
    {
        if (bean == null) {
            return null;
        }
        try {
            if (propName.indexOf(".") > 0) {
                Object value = bean;
                String[] props = StringUtils.split(propName, ".");
                for (int i = 0; i < props.length; i++) {
                    if (props[i].indexOf("(") != -1) {
                        value = PropertyUtils.getMappedProperty(value, props[i]);
                    } else if (props[i].indexOf("[") != -1) {
                        value = PropertyUtils.getIndexedProperty(value, props[i]);
                    } else {
                        value = PropertyUtils.getProperty(value, props[i]);
                    }
                    if (value == null) {
                        return null;
                    }
                }
                return value;
            }
            return PropertyUtils.getProperty(bean, propName);
        } catch (Throwable e) {
            service.logError("Error getting bean property: " + propName, e);
            throw new DYNException(" ",e);
        }
    }


    public static final String[] asStringArray(Object obj) {
        if (obj == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (obj instanceof String) {
            return new String[] { (String) obj };
        } else if (obj instanceof Object[]) {
            int length = ((Object[]) obj).length;
            String[] result = new String[length];
            for (int i = 0; i < length; i++) {
                Object value = ((Object[]) obj)[i];
                result[i] = (value != null) ? value.toString() : null;
            }
            return result;
        } else if (obj.getClass().isArray()) {
            String[] result = new String[Array.getLength(obj)];
            for (int i = 0; i < result.length; i++) {
                Object value = Array.get(obj, i);
                result[i] = (value != null) ? value.toString() : null;
            }
            return result;
        }
        return new String[] { obj.toString() };
    }
}
