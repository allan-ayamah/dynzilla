package com.atena.dynzilla.core;

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
}
