package com.github.boavenn.minject.instantiation.generic;

import com.github.boavenn.minject.exceptions.InjectionException;
import com.github.boavenn.minject.instantiation.*;
import com.github.boavenn.minject.utils.Types;
import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.injector.Injector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.*;
import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GenericClassInstantiator implements ClassInstantiator {
    private final Injector injector;
    private final InjectableConstructorResolver constructorResolver;
    private final InjectableFieldsResolver fieldsResolver;
    private final InjectableMethodsResolver methodsResolver;
    private final OverriddenMethodsStrategy overriddenMethodsStrategy;

    private final Set<Class<?>> classesInCreation = new HashSet<>();

    public static GenericClassInstantiatorBuilder using(Injector injector) {
        return new GenericClassInstantiatorBuilder(injector);
    }

    @Override
    public <T> T instantiateObjectOf(Class<T> cls) {
        if (classesInCreation.contains(cls)) {
            classesInCreation.clear();
            throw InjectionException.circularDependencyFoundIn(cls);
        }

        classesInCreation.add(cls);
        T instance = instantiateObjectVia(constructorResolver.findInjectableConstructorIn(cls));
        classesInCreation.remove(cls);

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
        var fields = allInjectableFieldsOf(cls);
        var methods = allInjectableMethodsOf(cls);
        var filteredMethods = overriddenMethodsStrategy.filterOverriddenMethods(methods);

        var fieldsItr = fields.iterator();
        var methodsItr = filteredMethods.iterator();

        while (fieldsItr.hasNext() && methodsItr.hasNext()) {
            injectFields(fieldsItr.next(), instance);
            injectMethods(methodsItr.next(), instance);
        }
    }

    private Queue<List<Field>> allInjectableFieldsOf(Class<?> cls) {
        Deque<List<Field>> fields = new LinkedList<>();

        fields.addFirst(fieldsResolver.findInjectableFieldsIn(cls));

        var superclass = cls.getSuperclass();
        while (isClassInjectable(superclass)) {
            fields.addFirst(fieldsResolver.findInjectableFieldsIn(superclass));
            superclass = superclass.getSuperclass();
        }

        return fields;
    }

    private Queue<List<Method>> allInjectableMethodsOf(Class<?> cls) {
        Deque<List<Method>> methods = new LinkedList<>();

        methods.addFirst(methodsResolver.findInjectableMethodsIn(cls));

        var superclass = cls.getSuperclass();
        while (isClassInjectable(superclass)) {
            methods.addFirst(methodsResolver.findInjectableMethodsIn(superclass));
            superclass = superclass.getSuperclass();
        }

        return methods;
    }

    private boolean isClassInjectable(Class<?> cls) {
        return cls != null && !cls.equals(Object.class);
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
