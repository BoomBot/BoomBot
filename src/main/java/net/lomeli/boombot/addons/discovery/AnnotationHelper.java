package net.lomeli.boombot.addons.discovery;

import com.google.common.collect.Maps;
import sun.reflect.annotation.AnnotationParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotationHelper {
    public static <A extends Annotation> A getAnnotation(Annotation annotation, Map<String, Object> defaultValues, Class<A> type) throws IllegalAccessException, InvocationTargetException {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Map<String, Object> values = Maps.newHashMap();
        for (Method method : annotationType.getDeclaredMethods()) {
            Object value = method.invoke(annotation, (Object[]) null);
            values.put(method.getName(), value);
        }
        if (defaultValues != null && !defaultValues.isEmpty())
            values.putAll(defaultValues);
        return (A) AnnotationParser.annotationForMap(type, values);
    }

    public static <A extends Annotation> A getAnnotationFromClass(Class<?> clazz, Map<String, Object> defaultValues, Class<A> type) throws IllegalAccessException, InvocationTargetException {
        A target = null;
        if (clazz.isAnnotationPresent(type)) {
            target = clazz.getAnnotation(type);
        } else {
            for (Annotation annotation : clazz.getAnnotations()) {
                if (annotation.annotationType().toString().equals(type.toString())) {
                    target = getAnnotation(annotation, defaultValues, type);
                    break;
                }
            }
        }
        return target;
    }

    public static <A extends Annotation> A getAnnotationFromMethod(Method method, Map<String, Object> defaultValues, Class<A> type) throws IllegalAccessException, InvocationTargetException {
        A target = null;
        if (method.isAnnotationPresent(type)) {
            target = method.getAnnotation(type);
        } else {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().toString().equals(type.toString())) {
                    target = getAnnotation(annotation, defaultValues, type);
                    break;
                }
            }
        }
        return target;
    }

    public static <A extends Annotation> A getAnnotationFromField(Field field, Map<String, Object> defaultValues, Class<A> type) throws IllegalAccessException, InvocationTargetException {
        A target = null;
        if (field.isAnnotationPresent(type)) {
            target = field.getAnnotation(type);
        } else {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation.annotationType().toString().equals(type.toString())) {
                    target = getAnnotation(annotation, defaultValues, type);
                    break;
                }
            }
        }
        return target;
    }
}
