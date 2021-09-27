package com.github.boavenn.minject.instantiaton.generic;

import com.github.boavenn.minject.exceptions.InjectionException;
import com.github.boavenn.minject.utils.Types;
import com.github.boavenn.minject.injector.ClassKey;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.instantiaton.ClassInstantiator;
import com.github.boavenn.minject.instantiaton.InjectableConstructorResolver;
import com.github.boavenn.minject.instantiaton.InjectableFieldsResolver;
import com.github.boavenn.minject.instantiaton.InjectableMethodsResolver;
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
                field.set(instance, resolveKey(fieldKey));
            } catch (Exception e) {
                throw InjectionException.injectableFieldValueSet(e);
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
                        .map(this::resolveKey)
                        .toArray(Object[]::new);
    }

    private Object resolveKey(ClassKey<?> classKey) {
        if (classKey.isProviderKey()) {
            var nestedTypeLiterals = Types.getTypeLiteralsOfNestedTypesIn(classKey.getTypeLiteral());
            var typeToProvide = nestedTypeLiterals.get(0);
            return injector.getProviderOf(classKey.with(typeToProvide));
        }
        return injector.getInstanceOf(classKey);
    }
}
