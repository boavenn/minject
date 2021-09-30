package com.github.boavenn.minject.instantiation.generic;

import com.github.boavenn.minject.instantiation.OverriddenMethodsStrategy;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;

@NoArgsConstructor(staticName = "create")
public class GenericOverriddenMethodsStrategy implements OverriddenMethodsStrategy {
    @Override
    public Queue<List<Method>> filterOverriddenMethods(Queue<List<Method>> inheritanceTreeMethods) {
        var inheritanceLevels = inheritanceTreeMethods.size();
        var filter = new OverridenMethodsFilter(inheritanceLevels);

        var inheritanceDepth = 0;
        for (var singleClassMethods : inheritanceTreeMethods) {
            for (var method : singleClassMethods) {
                filter.add(method, inheritanceDepth);
            }
            inheritanceDepth++;
        }

        return filter.getFilteredMethods();
    }
}
