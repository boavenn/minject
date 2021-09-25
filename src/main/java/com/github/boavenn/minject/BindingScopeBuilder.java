package com.github.boavenn.minject;

import java.lang.annotation.Annotation;

public interface BindingScopeBuilder {
    void in(Class<? extends Annotation> scope);
    void unscoped();
}
