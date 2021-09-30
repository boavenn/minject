package com.github.boavenn.minject.configuration.generic

import com.github.boavenn.minject.ClassKey
import com.github.boavenn.minject.binding.BindingRegistry
import com.github.boavenn.minject.configuration.Module
import com.github.boavenn.minject.configuration.RegistrationPolicy
import com.github.boavenn.minject.exceptions.BindingException
import com.github.boavenn.minject.scope.ScopeRegistry
import com.github.boavenn.minject.scope.Unscoped
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler
import spock.lang.Specification

class GenericBinderSpec extends Specification {
    def bindingRegistry = Mock(BindingRegistry)
    def scopeRegistry = Mock(ScopeRegistry)

    def sampleKeyName = "sampleName"
    def throwingStrategy = RegistrationPolicy.THROW.getStrategy()
    def replacingStrategy = RegistrationPolicy.REPLACE.getStrategy()

    def "bind() WHEN given a class key which is not registered SHOULD call binding registry"() {
        given:
        def classKey = ClassKey.of(String, sampleKeyName)
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        bindingRegistry.isRegistered(classKey) >> false

        when:
        binder.bind(classKey)

        then:
        1 * bindingRegistry.bind(classKey)
    }

    def "bind() WHEN given a class key which is already registered and replacing strategy is set SHOULD call binding registry"() {
        given:
        def classKey = ClassKey.of(String, sampleKeyName)
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, replacingStrategy, replacingStrategy)

        and:
        bindingRegistry.isRegistered(classKey) >> true

        when:
        binder.bind(classKey)

        then:
        1 * bindingRegistry.bind(classKey)
    }

    def "bind() WHEN given a class key which is already registered and throwing strategy is set SHOULD throw an exception"() {
        given:
        def classKey = ClassKey.of(String, sampleKeyName)
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        bindingRegistry.isRegistered(classKey) >> true

        when:
        binder.bind(classKey)

        then:
        BindingException ex = thrown()
        ex.getMessage() == BindingException.bindingAlreadyExists(classKey.toString()).getMessage()
    }

    def "bind() WHEN given a class which is not registered SHOULD call binding registry"() {
        given:
        def cls = String
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        bindingRegistry.isRegistered(cls) >> false

        when:
        binder.bind(cls)

        then:
        1 * bindingRegistry.bind(cls)
    }

    def "bind() WHEN given a class which is already registered and replacing strategy is set SHOULD call binding registry"() {
        given:
        def cls = String
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, replacingStrategy, replacingStrategy)

        and:
        bindingRegistry.isRegistered(cls) >> true

        when:
        binder.bind(cls)

        then:
        1 * bindingRegistry.bind(cls)
    }

    def "bind() WHEN given a class which is already registered and throwing strategy is set SHOULD throw an exception"() {
        given:
        def cls = String
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        bindingRegistry.isRegistered(cls) >> true

        when:
        binder.bind(cls)

        then:
        BindingException ex = thrown()
        ex.getMessage() == BindingException.bindingAlreadyExists(cls.getName()).getMessage()
    }

    def "bindScope() WHEN given a scope which is not registered SHOULD call scope registry"() {
        given:
        def scope = Unscoped
        def sampleHandler = UnscopedScopeHandler.empty()
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        scopeRegistry.isRegistered(scope) >> false

        when:
        binder.bindScope(scope, sampleHandler)

        then:
        1 * scopeRegistry.registerScope(scope, sampleHandler)
    }

    def "bindScope() WHEN given a scope which is already registered and replacing strategy is set SHOULD call scope registry"() {
        given:
        def scope = Unscoped
        def sampleHandler = UnscopedScopeHandler.empty()
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, replacingStrategy, replacingStrategy)

        and:
        scopeRegistry.isRegistered(scope) >> true

        when:
        binder.bindScope(scope, sampleHandler)

        then:
        1 * scopeRegistry.registerScope(scope, sampleHandler)
    }

    def "bindScope() WHEN given a scope which is already registered and throwing strategy is set SHOULD throw an exception"() {
        given:
        def scope = Unscoped
        def sampleHandler = UnscopedScopeHandler.empty()
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        and:
        scopeRegistry.isRegistered(scope) >> true

        when:
        binder.bindScope(scope, sampleHandler)

        then:
        BindingException ex = thrown()
        ex.getMessage() == BindingException.bindingAlreadyExists(scope.getName()).getMessage()
    }

    def "install() WHEN given a module which is not installed SHOULD add it to list of installed modules"() {
        given:
        def sampleModule = Mock(Module)
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        when:
        binder.install(sampleModule)

        then:
        binder.getInstalledModules().size() == 1
    }

    def "install() WHEN given a module which is already installed SHOULD not add it to list of installed modules"() {
        given:
        def sampleModule = Mock(Module)
        def binder = GenericBinder.using(bindingRegistry, scopeRegistry, throwingStrategy, throwingStrategy)

        when:
        binder.install(sampleModule)
        binder.install(sampleModule)

        then:
        binder.getInstalledModules().size() == 1
    }
}
