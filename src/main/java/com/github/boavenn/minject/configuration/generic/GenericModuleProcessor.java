package com.github.boavenn.minject.configuration.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.ConfigurationModule;
import com.github.boavenn.minject.configuration.ModuleProcessor;
import com.github.boavenn.minject.configuration.Provides;
import com.github.boavenn.minject.exceptions.InjectionException;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.utils.Types;

import javax.inject.Provider;
import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GenericModuleProcessor implements ModuleProcessor {
    @Override
    public void process(ConfigurationModule module, Binder binder, Injector injector) {
        var moduleClass = module.getClass();
        for (var method : moduleClass.getDeclaredMethods()) {
            if (!isConfigurationMethod(method) && isProvidingMethod(method)) {
                var returnTypeClassKey = ClassKey.from(method);
                var paramKeys = identifyParamsOf(method);
                var scope = findScopeOn(method).orElse(Unscoped.class);

                binder.bind(returnTypeClassKey)
                      .toProvider(createProviderFor(method, paramKeys, module, injector))
                      .in(scope);
            }
        }
    }

    private boolean isConfigurationMethod(Method method) {
        var methodName = method.getName();
        var paramTypes = method.getParameterTypes();
        return methodName.equals("configure") && paramTypes.length == 1 && paramTypes[0] == Binder.class;
    }

    private boolean isProvidingMethod(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(annotationType -> annotationType.equals(Provides.class));
    }

    private Optional<Class<? extends Annotation>> findScopeOn(Method method) {
        var annotations = method.getDeclaredAnnotations();
        return Arrays.stream(annotations)
                     .filter(annotation -> annotation.annotationType().isAnnotationPresent(Scope.class))
                     .findFirst()
                     .map(Annotation::annotationType);
    }

    private List<? extends ClassKey<?>> identifyParamsOf(Executable executable) {
        return Arrays.stream(executable.getParameters())
                     .map(ClassKey::from)
                     .toList();
    }

    @SuppressWarnings("unchecked")
    private <T> Provider<T> createProviderFor(Method method,
                                              List<? extends ClassKey<?>> paramKeys,
                                              ConfigurationModule module,
                                              Injector injector) {
        return () -> {
            try {
                return (T) method.invoke(module, resolveKeys(paramKeys, injector));
            } catch (Exception e) {
                throw InjectionException.injectableMethodInvocation(e);
            }
        };
    }

    private Object[] resolveKeys(List<? extends ClassKey<?>> classKeys, Injector injector) {
        return classKeys.stream()
                        .map(classKey -> resolveKey(classKey, injector))
                        .toArray(Object[]::new);
    }

    private Object resolveKey(ClassKey<?> classKey, Injector injector) {
        if (classKey.isProviderKey()) {
            var nestedTypeLiterals = Types.getTypeLiteralsOfNestedTypesIn(classKey.getTypeLiteral());
            var typeToProvide = nestedTypeLiterals.get(0);
            return injector.getProviderOf(classKey.with(typeToProvide));
        }
        return injector.getInstanceOf(classKey);
    }
}
