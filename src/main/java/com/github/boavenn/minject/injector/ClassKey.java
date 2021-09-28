package com.github.boavenn.minject.injector;

import com.github.boavenn.minject.exceptions.ClassKeyCreationException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ClassKey<T> {
    private final TypeLiteral<T> typeLiteral;
    private final String name;
    private final Annotation qualifier;

    public static <T> ClassKey<T> of(TypeLiteral<T> typeLiteral) {
        return new ClassKey<>(typeLiteral, null, null);
    }

    public static <T> ClassKey<T> of(TypeLiteral<T> typeLiteral, String name) {
        return new ClassKey<>(typeLiteral, name, null);
    }

    public static <T> ClassKey<T> of(TypeLiteral<T> typeLiteral, Annotation qualifier) {
        if (qualifier instanceof Named namedQualifier) {
            return new ClassKey<>(typeLiteral, namedQualifier.value(), null);
        }
        return new ClassKey<>(typeLiteral, null, qualifier);
    }

    public static <T> ClassKey<T> of(Class<T> cls) {
        return of(TypeLiteral.of(cls));
    }

    public static <T> ClassKey<T> of(Class<T> cls, String name) {
        return of(TypeLiteral.of(cls), name);
    }

    public static <T> ClassKey<T> of(Class<T> cls, Annotation qualifier) {
        return of(TypeLiteral.of(cls), qualifier);
    }

    public static <T> ClassKey<T> from(Class<T> cls) {
        return from(cls, cls);
    }

    private static <T> ClassKey<T> from(Class<T> cls, AnnotatedElement annotatedElement) {
        Annotation qualifier = findQualifierIn(annotatedElement.getDeclaredAnnotations());
        return of(cls, qualifier);
    }

    public static ClassKey<?> from(Field field) {
        return from(field.getGenericType(), field);
    }

    public static ClassKey<?> from(Method method) {
        var returnType = method.getGenericReturnType();

        if (returnType.equals(Void.TYPE)) {
            throw ClassKeyCreationException.methodOfVoidReturnType();
        }

        return from(returnType, method);
    }

    public static ClassKey<?> from(Parameter parameter) {
        return from(parameter.getParameterizedType(), parameter);
    }

    private static ClassKey<?> from(Type type, AnnotatedElement annotatedElement) {
        Annotation qualifier = findQualifierIn(annotatedElement.getDeclaredAnnotations());
        return of(TypeLiteral.of(type), qualifier);
    }

    private static Annotation findQualifierIn(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Qualifier.class))
                .findFirst()
                .orElse(null);
    }

    public boolean isProviderKey() {
        return typeLiteral.getRawType() == Provider.class;
    }

    public boolean isParameterized() {
        return typeLiteral.getType() instanceof ParameterizedType;
    }

    public boolean isQualified() {
        return name != null || qualifier != null;
    }

    public Type getRawType() {
        return typeLiteral.getRawType();
    }

    public ClassKey<Provider<T>> toProviderKey() {
        return new ClassKey<>(typeLiteral.asProviderType(), name, qualifier);
    }

    public <U> ClassKey<U> with(TypeLiteral<U> newTypeLiteral) {
        return new ClassKey<>(newTypeLiteral, name, qualifier);
    }
}
