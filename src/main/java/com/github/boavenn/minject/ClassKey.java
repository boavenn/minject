package com.github.boavenn.minject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ClassKey<T> {
    private final TypeLiteral<T> typeLiteral;
    @Getter private final String name;
    @Getter private final Annotation qualifier;

    public static <T> ClassKey<T> of(Class<T> cls) {
        return new ClassKey<>(TypeLiteral.of(cls), null, null);
    }

    public static <T> ClassKey<T> of(Class<T> cls, String name) {
        return new ClassKey<>(TypeLiteral.of(cls), name, null);
    }

    public static <T> ClassKey<T> of(Class<T> cls, Annotation qualifier) {
        if (qualifier instanceof Named namedQualifier) {
            return new ClassKey<>(TypeLiteral.of(cls), namedQualifier.value(), null);
        }
        return new ClassKey<>(TypeLiteral.of(cls), null, qualifier);
    }

    public static <T> ClassKey<T> from(Class<T> cls) {
        return from(cls, cls);
    }

    public static ClassKey<?> from(Field field) {
        return from(field.getType(), field);
    }

    public static ClassKey<?> from(Method method) {
        var returnType = method.getReturnType();

        if (returnType.equals(Void.TYPE)) {
            throw ClassKeyCreationException.voidMethod();
        }

        return from(method.getReturnType(), method);
    }

    public static ClassKey<?> from(Parameter parameter) {
        return from(parameter.getType(), parameter);
    }

    private static <T> ClassKey<T> from(Class<T> cls, AnnotatedElement annotatedElement) {
        Annotation qualifier = findQualifierIn(annotatedElement.getDeclaredAnnotations());
        return of(cls, qualifier);
    }

    private static Annotation findQualifierIn(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(Qualifier.class))
                .findFirst()
                .orElse(null);
    }

    public Class<T> getIdentifiedClass() {
        return typeLiteral.getRawType();
    }
}
