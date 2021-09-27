package com.github.boavenn.minject.scope;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface ScopeRegistry {
    void registerScope(Class<? extends Annotation> scope, ScopeHandler scopeHandler);
    Optional<ScopeHandler> getScopeHandlerFor(Class<? extends Annotation> scope);
    boolean isRegistered(Class<? extends Annotation> scope);
}
