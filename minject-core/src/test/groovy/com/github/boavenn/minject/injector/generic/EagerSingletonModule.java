package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.Provides;
import com.github.boavenn.minject.scope.EagerSingleton;
import lombok.Getter;

import javax.inject.Named;

@Getter
class EagerSingletonModule implements Module {
    public final static String className = "sampleClassName";
    public final static String providedClassName = "sampleProvidedClassName";

    private boolean providerMethodCalled;
    private boolean configurationProviderCalled;

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(UnqualifiedUnscopedClass.class, className))
              .toProvider(() -> {
                  configurationProviderCalled = true;
                  return new UnqualifiedUnscopedClass();
              })
              .asEagerSingleton();
    }

    @Provides
    @EagerSingleton
    @Named(providedClassName)
    public UnqualifiedSingletonClass providerMethod() {
        providerMethodCalled = true;
        return new UnqualifiedSingletonClass();
    }
}
