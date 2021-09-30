package com.github.boavenn.minject.instantiation.generic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

class OverridenMethodsFilter {
    private final Map<MethodSignature, List<DepthAwareMethod>> signatures = new HashMap<>();
    private final List<Set<Method>> filteredMethods;

    public OverridenMethodsFilter(int inheritanceLevels) {
        filteredMethods = new ArrayList<>(inheritanceLevels);
        for (int i = 0; i < inheritanceLevels; i++) {
            filteredMethods.add(new HashSet<>());
        }
    }

    public void add(Method method, int inheritanceDepth) {
        filteredMethods.get(inheritanceDepth).add(method);
        var signature = MethodSignature.of(method);

        if (!signatures.containsKey(signature)) {
            signatures.put(signature, new LinkedList<>());
            signatures.get(signature).add(DepthAwareMethod.of(method, inheritanceDepth));
        } else {
            boolean isOverriding = false;
            for (var rankedMethod : signatures.get(signature)) {
                var superclassMethod = rankedMethod.method;
                if (ovverrides(method, superclassMethod)) {
                    isOverriding = true;
                    filteredMethods.get(rankedMethod.depth).remove(superclassMethod);
                    rankedMethod.update(method, inheritanceDepth);
                }
            }
            if (!isOverriding) {
                signatures.get(signature).add(DepthAwareMethod.of(method, inheritanceDepth));
            }
        }
    }

    public Queue<List<Method>> getFilteredMethods() {
        Queue<List<Method>> result = new LinkedList<>();
        for (var methodSet : filteredMethods) {
            result.add(methodSet.stream().toList());
        }
        return result;
    }

    // See https://docs.oracle.com/javase/specs/jls/se16/html/jls-8.html#jls-8.4.8.1
    private boolean ovverrides(Method subclassMethod, Method superclassMethod) {
        var subclassMethodModifiers = subclassMethod.getModifiers();

        var isPublic = Modifier.isPublic(subclassMethodModifiers);
        var isProtected = Modifier.isProtected(subclassMethodModifiers);
        var isPrivate = Modifier.isPrivate(subclassMethodModifiers);

        return !isPrivate && (isProtected || isPublic || declaredWithinSamePackage(subclassMethod, superclassMethod));
    }

    private boolean declaredWithinSamePackage(Method a, Method b) {
        var classA = a.getDeclaringClass();
        var classB = b.getDeclaringClass();
        return classA.getPackage().equals(classB.getPackage());
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    private static class MethodSignature {
        private final String name;
        private final Class<?>[] parameterTypes;

        public static MethodSignature of(Method method) {
            return new MethodSignature(method.getName(), method.getParameterTypes());
        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class DepthAwareMethod {
        private Method method;
        private int depth;

        public void update(Method method, int inheritanceDepth) {
            this.method = method;
            this.depth = inheritanceDepth;
        }
    }
}
