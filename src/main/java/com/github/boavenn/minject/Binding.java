package com.github.boavenn.minject;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

public interface Binding<T> {
    ClassKey<T> getClassKey();
    Provider<? extends T> getProvider();
    Class<? extends Annotation> getScope();
}
