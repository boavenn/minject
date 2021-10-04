package com.github.boavenn.minject.utils;

import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;

public abstract class ReflectionUtils {
    public static Optional<Class<? extends Annotation>> findScopeOn(AnnotatedElement annotatedElement) {
        var annotations = annotatedElement.getDeclaredAnnotations();
        return Arrays.stream(annotations)
                     .filter(annotation -> annotation.annotationType().isAnnotationPresent(Scope.class))
                     .findFirst()
                     .map(Annotation::annotationType);
    }
}
