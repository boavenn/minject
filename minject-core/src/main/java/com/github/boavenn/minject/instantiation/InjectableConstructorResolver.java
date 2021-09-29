package com.github.boavenn.minject.instantiation;

import java.lang.reflect.Constructor;

public interface InjectableConstructorResolver {
    <T> Constructor<T> findInjectableConstructorIn(Class<T> cls);
}
