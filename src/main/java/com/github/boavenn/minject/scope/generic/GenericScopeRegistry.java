package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.scope.ScopeHandler;
import com.github.boavenn.minject.scope.ScopeRegistry;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(staticName = "empty")
public class GenericScopeRegistry implements ScopeRegistry {
    private final Map<Class<? extends Annotation>, ScopeHandler> scopeHandlers = new HashMap<>();

    @Override
    public void registerScope(Class<? extends Annotation> scope, ScopeHandler scopeHandler) {
        scopeHandlers.put(scope, scopeHandler);
    }

    @Override
    public Optional<ScopeHandler> getScopeHandlerFor(Class<? extends Annotation> scope) {
        return Optional.ofNullable(scopeHandlers.get(scope));
    }

    @Override
    public boolean isRegistered(Class<? extends Annotation> scope) {
        return scopeHandlers.containsKey(scope);
    }
}
