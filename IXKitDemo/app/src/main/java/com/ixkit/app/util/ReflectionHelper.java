/**
 * ReflectionHelper
 *
 *
 * For more information, visit the project page:
 * https://github.com/icoco/ixkit
 *
 * @author Robin Cheung <iRobinCheung@hotmail.com>
 * @version 1.0.1
 */
package com.ixkit.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ReflectionHelper {

    public static List<Field> getAllFields(Class<?> cls) {
        final List<Field> fields = new ArrayList<Field>();
        final Field[] declaredFields = cls.getDeclaredFields();
        Collections.addAll(fields, declaredFields);
        final Class<?> superClass = cls.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            final List<Field> superFields = getAllFields(superClass);
            fields.addAll(superFields);
        }
        return fields;
    }

    public static Field getField(Class<?> cls, String name) {
        final Class<?> superClass = cls.getSuperclass();

        try {
            final Field field = cls.getDeclaredField(name);
            if (field != null) {
                return field;
            } else if (superClass != null) {
                return getField(superClass, name);
            } else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            // LogHelper.verbose(ReflectionHelper.class, "Could not find field " + name + " in " + cls);
            e.printStackTrace();

            if (superClass != null) {
                return getField(superClass, name);
            } else {
                return null;
            }
        }
    }

    public static <T> T getValueOf(Field field, Object item) {
        if (field == null) {
            return null;
        }

        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return (T) field.get(item);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not read value from Field!", e);
        }
    }

    public static <T> T getValueOf(String name, Object item) {
        final Field field = getField(item.getClass(), name);
        if (field != null) {
            return getValueOf(field, item);
        }
        return null;
    }

    public static boolean setValueOf(Field field, Object item, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            field.set(item, value);
            return true;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not read value from Field!", e);
        }
    }

    public static <T> Class<T> getClassOf(T obj) {
        return (Class<T>) obj.getClass();
    }

    public static Class<?> getGenericType(Field field) {
        final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    public static void invoke(Method method, Object receiver, Object... arguments) {
        if (method == null || receiver == null) {
            return;
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        try {
            method.invoke(receiver, arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not invoke method!", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Could not invoke method!", e);
        }
    }

    public static Object invoke(String className, String methodName,  Class[] parameterTypes,
                                Object target, Object... arguments) throws Exception {

        Class cls = Class.forName(className);
        Method method = cls.getDeclaredMethod(methodName, parameterTypes);
        //@step call static method only ?
        Object obj = method.invoke(cls, arguments);

        return obj;

    }


    public static Object invoke( Object target, String methodName, Class[] parameterTypes,  Object... arguments) throws Exception {

        Class cls = target.getClass();
        Method method = cls.getMethod(methodName, parameterTypes);
        Object obj = method.invoke(target, arguments);

        return obj;

    }
}