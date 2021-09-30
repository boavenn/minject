package com.github.boavenn.minject.instantiation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;

public interface OverriddenMethodsStrategy {
    Queue<List<Method>> filterOverriddenMethods(Queue<List<Method>> inheritanceTreeMethods);
}
