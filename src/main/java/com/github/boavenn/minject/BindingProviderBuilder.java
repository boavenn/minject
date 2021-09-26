package com.github.boavenn.minject;

import javax.inject.Provider;

public interface BindingProviderBuilder<T> {
    <U extends T> BindingScopeBuilder to(Class<U> cls);
    <U extends T> BindingScopeBuilder toProvider(Provider<U> provider);
    <U extends T> void toInstance(U instance);
}
