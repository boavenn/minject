package com.github.boavenn.minject.binding.generic;

import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.injector.ClassKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class GenericBinding<T> implements Binding<T> {
    private final ClassKey<T> classKey;
    private final Provider<? extends T> provider;
    private final Class<? extends Annotation> scope;
}
