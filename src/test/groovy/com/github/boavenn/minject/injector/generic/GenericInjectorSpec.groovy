package com.github.boavenn.minject.injector.generic

import com.github.boavenn.minject.binding.BindingRegistry
import com.github.boavenn.minject.binding.generic.GenericBinding
import com.github.boavenn.minject.exceptions.InjectorException
import com.github.boavenn.minject.injector.ClassKey
import com.github.boavenn.minject.scope.ScopeRegistry
import com.github.boavenn.minject.scope.Unscoped
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler
import spock.lang.Specification

import javax.inject.Singleton

class GenericInjectorSpec extends Specification {
    def bindingRegistry = Mock(BindingRegistry)
    def scopeRegistry = Mock(ScopeRegistry)
    def injector = GenericInjector.builder()
                                  .using(injector -> bindingRegistry)
                                  .using(scopeRegistry)
                                  .build()

    def sampleIntegerValue = Integer.MAX_VALUE // To prevent integer internal cache

    def "getInstanceOf() WHEN given a class key and binding is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer)

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.empty()

        when:
        injector.getInstanceOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(classKey).getMessage()
    }

    def "getInstanceOf() WHEN given a class key and scope handler is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getInstanceOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getInstanceOf() WHEN given a class key SHOULD scope binding default provider and return scoped instance"() {
        given:
        def classKey = ClassKey.of(Integer)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(classKey)
        def instance2 = injector.getInstanceOf(classKey)
        def instance3 = injector.getInstanceOf(classKey)

        then:
        instance1 == instance2
        instance2 == instance3
        instance3 == instance1
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }

    def "getInstanceOf() WHEN given a class and binding is not found SHOULD throw an exception"() {
        given:
        def cls = Integer

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.empty()

        when:
        injector.getInstanceOf(cls)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(ClassKey.of(cls)).getMessage()
    }

    def "getInstanceOf() WHEN given a class and scope handler is not found SHOULD throw an exception"() {
        given:
        def cls = Integer
        def scope = Singleton
        def sampleBinding = GenericBinding.of(ClassKey.of(cls), () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getInstanceOf(cls)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getInstanceOf() WHEN given a class SHOULD scope binding default provider and return scoped instance"() {
        given:
        def cls = Integer
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(ClassKey.of(cls), () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(cls)
        def instance2 = injector.getInstanceOf(cls)
        def instance3 = injector.getInstanceOf(cls)

        then:
        instance1 == instance2
        instance2 == instance3
        instance3 == instance1
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }

    def "getProviderOf() WHEN given a class key and binding is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer)

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.empty()

        when:
        injector.getProviderOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(classKey).getMessage()
    }

    def "getProviderOf() WHEN given a class key and scope handler is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getProviderOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getProviderOf() WHEN given a class key SHOULD scope binding default provider and return scoped instance provider"() {
        given:
        def classKey = ClassKey.of(Integer)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(classKey)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 == instance2
        instance2 == instance3
        instance3 == instance1
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }

    def "getProviderOf() WHEN given a class and binding is not found SHOULD throw an exception"() {
        given:
        def cls = Integer

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.empty()

        when:
        injector.getProviderOf(cls)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(ClassKey.of(cls)).getMessage()
    }

    def "getProviderOf() WHEN given a class and scope handler is not found SHOULD throw an exception"() {
        given:
        def cls = Integer
        def scope = Singleton
        def sampleBinding = GenericBinding.of(ClassKey.of(cls), () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getProviderOf(cls)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getProviderOf() WHEN given a class SHOULD scope binding default provider and return scoped instance provider"() {
        given:
        def cls = Integer
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(ClassKey.of(cls), () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        bindingRegistry.getBindingFor(ClassKey.of(cls)) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(cls)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 == instance2
        instance2 == instance3
        instance3 == instance1
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }
}
