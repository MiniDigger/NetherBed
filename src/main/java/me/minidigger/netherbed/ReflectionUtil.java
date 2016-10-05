package me.minidigger.netherbed;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Martin on 05.10.2016.
 */
public class ReflectionUtil {

    public static void invokeMethod(Class clazz, String methodName, Class[] parameters, Object... args) {
        try {
            @SuppressWarnings("unchecked")
            Method method = clazz.getDeclaredMethod(methodName, parameters);
            method.setAccessible(true);
            method.invoke(null, args);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setFinalStatic(Class clazz, String fieldName, Object newValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
