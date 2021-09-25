package com.github.boavenn.minject;

public interface ClassInstantiator {
    <T> T instantiateObjectOf(Class<T> cls);
}
