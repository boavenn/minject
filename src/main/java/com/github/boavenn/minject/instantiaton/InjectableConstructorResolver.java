package com.github.boavenn.minject.instantiaton;

import java.lang.reflect.Constructor;

public interface InjectableConstructorResolver {
    <T> Constructor<T> findInjectableConstructorIn(Class<T> cls);
}
