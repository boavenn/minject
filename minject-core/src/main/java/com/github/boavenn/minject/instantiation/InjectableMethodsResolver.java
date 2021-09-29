package com.github.boavenn.minject.instantiation;

import java.lang.reflect.Method;
import java.util.List;

public interface InjectableMethodsResolver {
    <T> List<Method> findInjectableMethodsIn(Class<T> cls);
}
