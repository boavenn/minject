package com.github.boavenn.minject.instantiaton.generic;

import com.github.boavenn.minject.instantiaton.OverriddenMethodsStrategy;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@NoArgsConstructor(staticName = "create")
public class GenericOverriddenMethodsStrategy implements OverriddenMethodsStrategy {
    @Override
    public List<Method> removeOverriddenMethods(List<Method> subclassMethods, List<Method> superclassMethods) {
        Map<MethodSignature, Method> methodSignatures = new HashMap<>();

        for (var superclassMethod : superclassMethods) {
            methodSignatures.put(MethodSignature.of(superclassMethod), superclassMethod);
        }

        for (var subclassMethod : subclassMethods) {
            var signature = MethodSignature.of(subclassMethod);
            if (methodSignatures.containsKey(signature)) {
                var superclassMethod = methodSignatures.get(signature);
                if (ovverrides(subclassMethod, superclassMethod)) {
                    methodSignatures.remove(signature);
                }
            }
        }

        return new LinkedList<>(methodSignatures.values());
    }

    // See https://docs.oracle.com/javase/specs/jls/se16/html/jls-8.html#jls-8.4.8.1
    private boolean ovverrides(Method subclassMethod, Method superclassMethod) {
        var subclassMethodModifiers = subclassMethod.getModifiers();

        var isPublic = Modifier.isPublic(subclassMethodModifiers);
        var isProtected = Modifier.isProtected(subclassMethodModifiers);
        var isPrivate = Modifier.isPrivate(subclassMethodModifiers);

        return !isPrivate && (isProtected || isPublic || declaredWithingSamePackage(subclassMethod, superclassMethod));
    }

    private boolean declaredWithingSamePackage(Method a, Method b) {
        var classA = a.getDeclaringClass();
        var classB = b.getDeclaringClass();
        return classA.getPackage().equals(classB.getPackage());
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
    private static class MethodSignature {
        private final String name;
        private final Class<?>[] parameterTypes;

        public static MethodSignature of(Method method) {
            return new MethodSignature(method.getName(), method.getParameterTypes());
        }
    }
}
