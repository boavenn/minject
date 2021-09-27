package com.github.boavenn.minject.instantiaton;

public interface ClassInstantiator {
    <T> T instantiateObjectOf(Class<T> cls);
}
