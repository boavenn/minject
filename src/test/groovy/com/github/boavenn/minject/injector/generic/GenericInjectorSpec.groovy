package com.github.boavenn.minject.injector.generic

import com.github.boavenn.minject.binding.BindingProviderBuilder
import com.github.boavenn.minject.binding.BindingRegistry
import com.github.boavenn.minject.binding.BindingScopeBuilder
import com.github.boavenn.minject.binding.generic.GenericBinding
import com.github.boavenn.minject.exceptions.InjectorException
import com.github.boavenn.minject.ClassKey
import com.github.boavenn.minject.scope.ScopeRegistry
import com.github.boavenn.minject.scope.Unscoped
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler
import spock.lang.Specification

import javax.inject.Singleton

class GenericInjectorSpec extends Specification {
    def scopeBuilder = Mock(BindingScopeBuilder)
    def providerBuilder = Mock(BindingProviderBuilder)
    def bindingRegistry = Mock(BindingRegistry)
    def scopeRegistry = Mock(ScopeRegistry)
    def injector = GenericInjector.builder()
                                  .usingBindingRegistry(injector -> bindingRegistry)
                                  .usingScopeRegistry(injector -> scopeRegistry)
                                  .build()

    def sampleIntegerValue = Integer.MAX_VALUE // To prevent integer internal cache
    def sampleNamedQualifierValue = "sampleQualifierName"

    def "getInstanceOf() WHEN given a qualified class key and binding is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)

        and:
        bindingRegistry.isRegistered(classKey) >> false
        bindingRegistry.getBindingFor(classKey) >> Optional.empty()

        when:
        injector.getInstanceOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(classKey).getMessage()
    }

    def "getInstanceOf() WHEN given a qualified class key and scope handler is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.isRegistered(classKey) >> true
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getInstanceOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getInstanceOf() WHEN given a qualified class key SHOULD scope binding default provider and return scoped instance"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        bindingRegistry.isRegistered(classKey) >> true
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

    def "getInstanceOf() WHEN given an unqualified unscoped class key and binding is not found SHOULD create implicit binding for it"() {
        given:
        def classKey = ClassKey.of(UnqualifiedUnscopedClass)
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedUnscopedClass(), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        2 * bindingRegistry.isRegistered(classKey) >> true
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedUnscopedClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(classKey)
        def instance2 = injector.getInstanceOf(classKey)
        def instance3 = injector.getInstanceOf(classKey)

        then:
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }

    def "getInstanceOf() WHEN given an unqualified scoped class key and binding is not found SHOULD create implicit binding for it"() {
        given:
        def classKey = ClassKey.of(UnqualifiedSingletonClass)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedSingletonClass(), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        2 * bindingRegistry.isRegistered(classKey) >> true
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedSingletonClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(classKey)
        def instance2 = injector.getInstanceOf(classKey)
        def instance3 = injector.getInstanceOf(classKey)

        then:
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }

    def "getInstanceOf() WHEN given an unqualified unscoped class and binding is not found SHOULD create implicit binding for it"() {
        given:
        def cls = UnqualifiedUnscopedClass
        def classKey = ClassKey.of(cls)
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedUnscopedClass(), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        2 * bindingRegistry.isRegistered(classKey) >> true
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedUnscopedClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(cls)
        def instance2 = injector.getInstanceOf(cls)
        def instance3 = injector.getInstanceOf(cls)

        then:
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }

    def "getInstanceOf() WHEN given an unqualified scoped class and binding is not found SHOULD create implicit binding for it"() {
        given:
        def cls = UnqualifiedSingletonClass
        def classKey = ClassKey.of(cls)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedSingletonClass(), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        2 * bindingRegistry.isRegistered(classKey) >> true
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedSingletonClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def instance1 = injector.getInstanceOf(cls)
        def instance2 = injector.getInstanceOf(cls)
        def instance3 = injector.getInstanceOf(cls)

        then:
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }

    def "getProviderOf() WHEN given a qualified class key and binding is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)

        and:
        bindingRegistry.isRegistered(classKey) >> false
        bindingRegistry.getBindingFor(classKey) >> Optional.empty()

        when:
        injector.getProviderOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.bindingNotRegistered(classKey).getMessage()
    }


    def "getProviderOf() WHEN given a qualified class key and scope handler is not found SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)

        and:
        bindingRegistry.isRegistered(classKey) >> true
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.empty()

        when:
        injector.getProviderOf(classKey)

        then:
        InjectorException ex = thrown()
        ex.getMessage() == InjectorException.scopeNotRegistered(scope).getMessage()
    }

    def "getProviderOf() WHEN given a qualified class key SHOULD scope binding default provider and return scoped instance provider"() {
        given:
        def classKey = ClassKey.of(Integer, sampleNamedQualifierValue)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> Integer.valueOf(sampleIntegerValue), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        bindingRegistry.isRegistered(classKey) >> true
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

    def "getProviderOf() WHEN given an unqualified unscoped class key and binding is not found SHOULD create implicit binding for it"() {
        given:
        def classKey = ClassKey.of(UnqualifiedUnscopedClass)
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedUnscopedClass(), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedUnscopedClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(classKey)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }

    def "getProviderOf() WHEN given an unqualified scoped class key and binding is not found SHOULD create implicit binding for it"() {
        given:
        def classKey = ClassKey.of(UnqualifiedSingletonClass)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedSingletonClass(), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedSingletonClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(classKey)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }

    def "getProviderOf() WHEN given an unqualified unscoped class and binding is not found SHOULD create implicit binding for it"() {
        given:
        def cls = UnqualifiedUnscopedClass
        def classKey = ClassKey.of(cls)
        def scope = Unscoped
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedUnscopedClass(), scope)
        def sampleScopeHandler = UnscopedScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedUnscopedClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(cls)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 !== instance2
        instance2 !== instance3
        instance3 !== instance1
    }

    def "getProviderOf() WHEN given an unqualified scoped class and binding is not found SHOULD create implicit binding for it"() {
        given:
        def cls = UnqualifiedSingletonClass
        def classKey = ClassKey.of(cls)
        def scope = Singleton
        def sampleBinding = GenericBinding.of(classKey, () -> new UnqualifiedSingletonClass(), scope)
        def sampleScopeHandler = SingletonScopeHandler.empty()

        and:
        1 * bindingRegistry.isRegistered(classKey) >> false
        1 * bindingRegistry.bind(classKey) >> providerBuilder
        1 * providerBuilder.to(UnqualifiedSingletonClass) >> scopeBuilder
        1 * scopeBuilder.in(scope)
        bindingRegistry.getBindingFor(classKey) >> Optional.of(sampleBinding)
        scopeRegistry.getScopeHandlerFor(scope) >> Optional.of(sampleScopeHandler)

        when:
        def provider = injector.getProviderOf(cls)
        def instance1 = provider.get()
        def instance2 = provider.get()
        def instance3 = provider.get()

        then:
        instance1 === instance2
        instance2 === instance3
        instance3 === instance1
    }
}
