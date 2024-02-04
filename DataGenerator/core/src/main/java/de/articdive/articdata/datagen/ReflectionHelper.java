package de.articdive.articdata.datagen;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public final class ReflectionHelper {

    public static Class<?> getHiddenClass(Class<?> parent, String className) {
        if (parent == null) {
            throw new NullPointerException("Parent cannot be null.");
        }
        Optional<Class<?>> hiddenClassOpt = Arrays.stream(parent.getDeclaredClasses())
                .filter(aClass -> aClass.getSimpleName().equalsIgnoreCase(className)
                ).findFirst();
        if (hiddenClassOpt.isEmpty()) {
            throw new IllegalStateException("Could not find 'PacketSet' Class.");
        }
        return hiddenClassOpt.get();
    }

    @SuppressWarnings("unchecked")
    public static <T, I> T getHiddenField(Class<T> fieldType, String fieldName, Class<I> instanceClass, I instance) {
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

    @SuppressWarnings("unchecked")
    public static <T, I> T callHiddenMethod(Class<T> responseType, String methodName, Class<I> instanceClass, I instance, Map<Class<?>, ?> parameters) {
        if (responseType == null) {
            throw new NullPointerException("Response type cannot be null.");
        }
        if (methodName == null) {
            throw new NullPointerException("Field Name cannot be null.");
        }
        try {
            Method m = instanceClass.getDeclaredMethod(methodName, parameters.keySet().toArray(Class[]::new));
            m.setAccessible(true);
            return (T) m.invoke(instance, parameters.values().toArray(Object[]::new));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }
}
