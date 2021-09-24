package com.github.boavenn.minject;

import java.lang.reflect.Constructor;

public interface InjectableConstructorResolver {
    <T> Constructor<T> findInjectableConstructorIn(Class<T> cls);
}
