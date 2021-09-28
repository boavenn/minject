package com.github.boavenn.minject.instantiation.generic;

import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.instantiation.InjectableConstructorResolver;
import com.github.boavenn.minject.instantiation.InjectableFieldsResolver;
import com.github.boavenn.minject.instantiation.InjectableMethodsResolver;
import com.github.boavenn.minject.instantiation.OverriddenMethodsStrategy;

public class GenericClassInstantiatorBuilder {
    private final Injector injector;
    private InjectableConstructorResolver injectableConstructorResolver;
    private InjectableFieldsResolver injectableFieldsResolver;
    private InjectableMethodsResolver injectableMethodsResolver;
    private OverriddenMethodsStrategy overriddenMethodsStrategy;

    public GenericClassInstantiatorBuilder(Injector injector) {
        this.injector = injector;
        this.injectableConstructorResolver = GenericInjectableConstructorResolver.create();
        this.injectableFieldsResolver = GenericInjectableFieldsResolver.create();
        this.injectableMethodsResolver = GenericInjectableMethodsResolver.create();
        this.overriddenMethodsStrategy = GenericOverriddenMethodsStrategy.create();
    }

    public GenericClassInstantiatorBuilder with(InjectableConstructorResolver injectableConstructorResolver) {
        this.injectableConstructorResolver = injectableConstructorResolver;
        return this;
    }

    public GenericClassInstantiatorBuilder with(InjectableFieldsResolver injectableFieldsResolver) {
        this.injectableFieldsResolver = injectableFieldsResolver;
        return this;
    }

    public GenericClassInstantiatorBuilder with(InjectableMethodsResolver injectableMethodsResolver) {
        this.injectableMethodsResolver = injectableMethodsResolver;
        return this;
    }

    public GenericClassInstantiatorBuilder with(OverriddenMethodsStrategy overriddenMethodsStrategy) {
        this.overriddenMethodsStrategy = overriddenMethodsStrategy;
        return this;
    }

    public GenericClassInstantiator build() {
        return new GenericClassInstantiator(injector,
                                            injectableConstructorResolver,
                                            injectableFieldsResolver,
                                            injectableMethodsResolver,
                                            overriddenMethodsStrategy);
    }
}
