package com.github.boavenn.minject.binding;

import java.lang.annotation.Annotation;

public interface BindingScopeBuilder {
    void in(Class<? extends Annotation> scope);
    void unscoped();
}
