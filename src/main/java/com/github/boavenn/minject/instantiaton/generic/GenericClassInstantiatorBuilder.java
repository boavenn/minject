package com.github.boavenn.minject.instantiaton.generic;

import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.instantiaton.InjectableConstructorResolver;
import com.github.boavenn.minject.instantiaton.InjectableFieldsResolver;
import com.github.boavenn.minject.instantiaton.InjectableMethodsResolver;

public class GenericClassInstantiatorBuilder {
    private final Injector injector;
    private InjectableConstructorResolver injectableConstructorResolver;
    private InjectableFieldsResolver injectableFieldsResolver;
    private InjectableMethodsResolver injectableMethodsResolver;

    public GenericClassInstantiatorBuilder(Injector injector) {
        this.injector = injector;
        this.injectableConstructorResolver = GenericInjectableConstructorResolver.create();
        this.injectableFieldsResolver = GenericInjectableFieldsResolver.create();
        this.injectableMethodsResolver = GenericInjectableMethodsResolver.create();
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

    public GenericClassInstantiator build() {
        return new GenericClassInstantiator(injector,
                                            injectableConstructorResolver,
                                            injectableFieldsResolver,
                                            injectableMethodsResolver);
    }
}