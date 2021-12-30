package de.articdive.articdata.datagen;

import java.lang.reflect.Field;

public final class ReflectionHelper {

    @SuppressWarnings("unchecked")
    public static <T, R> T getHiddenField(Class<T> fieldType, String fieldName, Class<R> instanceClass, R instance) {
        if (fieldType == null) {
            throw new NullPointerException("Field type cannot be null.");
        }
        if (fieldName == null) {
            throw new NullPointerException("Field Name cannot be null.");
        }
        try {
            Field f = instanceClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
