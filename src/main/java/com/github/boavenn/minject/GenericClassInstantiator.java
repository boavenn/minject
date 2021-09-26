package com.github.boavenn.minject;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class GenericClassInstantiator implements ClassInstantiator {
    private final Injector injector;
    private final InjectableConstructorResolver constructorResolver;
    private final InjectableFieldsResolver fieldsResolver;
    private final InjectableMethodsResolver methodsResolver;

    public static GenericClassInstantiatorBuilder using(Injector injector) {
        return new GenericClassInstantiatorBuilder(injector);
    }

    @Override
    public <T> T instantiateObjectOf(Class<T> cls) {
        T instance = instantiateObjectVia(constructorResolver.findInjectableConstructorIn(cls));
        injectMembers(cls, instance);
        return instance;
    }

    private <T> T instantiateObjectVia(Constructor<T> constructor) {
        constructor.setAccessible(true);
        var paramKeys = identifyParamsOf(constructor);
        var dependenciesToInject = resolveKeys(paramKeys);
        try {
            return constructor.newInstance(dependenciesToInject);
        } catch (Exception e) {
            throw InjectionException.injectableConstructorInvocation(e);
        }
    }

    private <T> void injectMembers(Class<? super T> cls, T instance) {
        if (hasInjectableSuperclass(cls)) {
            injectMembers(cls.getSuperclass(), instance);
        }
        injectFields(fieldsResolver.findInjectableFieldsIn(cls), instance);
        injectMethods(methodsResolver.findInjectableMethodsIn(cls), instance);
    }

    private boolean hasInjectableSuperclass(Class<?> cls) {
        var superclass = cls.getSuperclass();
        return superclass != null && !superclass.equals(Object.class);
    }

    private void injectFields(List<Field> fields, Object instance) {
        fields.forEach(field -> {
            field.setAccessible(true);
            var fieldKey = ClassKey.from(field);
            try {
                field.set(instance, injector.getInstanceOf(fieldKey));
            } catch (Exception e) {
                throw InjectionException.injectableFieldSet(e);
            }
        });
    }

    private void injectMethods(List<Method> methods, Object instance) {
        methods.forEach(method -> {
            method.setAccessible(true);
            var paramKeys = identifyParamsOf(method);
            var dependenciesToInject = resolveKeys(paramKeys);
            try {
                method.invoke(instance, dependenciesToInject);
            } catch (Exception e) {
                throw InjectionException.injectableMethodInvocation(e);
            }
        });
    }

    private List<? extends ClassKey<?>> identifyParamsOf(Executable executable) {
        return Arrays.stream(executable.getParameters())
                     .map(ClassKey::from)
                     .toList();
    }

    private Object[] resolveKeys(List<? extends ClassKey<?>> classKeys) {
        return classKeys.stream()
                        .map(injector::getInstanceOf)
                        .toArray(Object[]::new);
    }
}