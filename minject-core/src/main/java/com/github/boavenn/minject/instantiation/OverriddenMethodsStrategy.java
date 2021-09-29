package com.github.boavenn.minject.instantiation;

import java.lang.reflect.Method;
import java.util.List;

public interface OverriddenMethodsStrategy {
    List<Method> removeOverriddenMethods(List<Method> subclassMethods, List<Method> superclassMethods);
}
