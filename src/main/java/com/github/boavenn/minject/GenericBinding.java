package com.github.boavenn.minject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericBinding<T> implements Binding<T> {
    private final ClassKey<T> classKey;
    private final Provider<T> provider;
    private final Class<? extends Annotation> scope;

    public static <T> GenericBinding<T> of(ClassKey<T> classKey,
                                        Provider<T> provider,
                                        Class<? extends Annotation> scope) {
        return new GenericBinding<>(classKey, provider, scope);
    }
}
