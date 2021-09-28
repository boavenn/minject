package com.github.boavenn.minject.binding.generic


import com.github.boavenn.minject.binding.generic.GenericBinding
import com.github.boavenn.minject.binding.generic.GenericBindingRegistry
import com.github.boavenn.minject.injector.ClassKey
import com.github.boavenn.minject.instantiation.ClassInstantiator
import com.github.boavenn.minject.scope.Unscoped
import spock.lang.Specification

import javax.inject.Singleton

class GenericBindingRegistrySpec extends Specification {
    def classInstantiator = Mock(ClassInstantiator)
    def registry = GenericBindingRegistry.using(classInstantiator)

    def sampleNamedQualifierName = "sampleNamedQualifier"

    def "bind() WHEN binding a class key to an instance SHOULD register such binding correctly"() {
        given:
        def instance = new SubclassClass("", "")
        def classKey = ClassKey.of(SuperclassClass, sampleNamedQualifierName)

        when:
        registry.bind(classKey)
                .toInstance(instance)

        def binding =  registry.getBindingFor(classKey).get()

        then:
        binding.getClassKey() == classKey
        binding.getProvider().get() === instance
        binding.getScope() == Singleton
    }

    def "bind() WHEN binding a class to an instance SHOULD register such binding correctly"() {
        given:
        def instance = new SubclassClass("", "")

        when:
        registry.bind(SuperclassClass)
                .toInstance(instance)

        def binding =  registry.getBindingFor(SuperclassClass).get()

        then:
        binding.getClassKey() == ClassKey.of(SuperclassClass)
        binding.getProvider().get() === instance
        binding.getScope() == Singleton
    }

    def "bind() WHEN binding a class key to a provider in some scope SHOULD register such binding correctly"() {
        given:
        def classKey = ClassKey.of(SuperclassClass, sampleNamedQualifierName)

        when:
        registry.bind(classKey)
                .toProvider(() -> new SubclassClass("", ""))
                .unscoped()

        def binding =  registry.getBindingFor(classKey).get()

        then:
        binding.getClassKey() == classKey
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Unscoped
    }

    def "bind() WHEN binding a class to a provider in some scope SHOULD register such binding correctly"() {
        when:
        registry.bind(SuperclassClass)
                .toProvider(() -> new SubclassClass("", ""))
                .unscoped()

        def binding =  registry.getBindingFor(SuperclassClass).get()

        then:
        binding.getClassKey() == ClassKey.of(SuperclassClass)
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Unscoped
    }

    def "bind() WHEN binding a class key to a class in some scope SHOULD register such binding correctly"() {
        given:
        def classKey = ClassKey.of(SuperclassClass, sampleNamedQualifierName)

        and:
        classInstantiator.instantiateObjectOf(SubclassClass) >> new SubclassClass("", "")

        when:
        registry.bind(classKey)
                .to(SubclassClass)
                .in(Singleton)

        def binding =  registry.getBindingFor(classKey).get()

        then:
        binding.getClassKey() == classKey
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Singleton
    }

    def "bind() WHEN binding a class to a class in some scope SHOULD register such binding correctly"() {
        given:
        classInstantiator.instantiateObjectOf(SubclassClass) >> new SubclassClass("", "")

        when:
        registry.bind(SuperclassClass)
                .to(SubclassClass)
                .in(Singleton)

        def binding =  registry.getBindingFor(SuperclassClass).get()

        then:
        binding.getClassKey() == ClassKey.of(SuperclassClass)
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Singleton
    }

    def "getBindingFor() WHEN given a class key which is registered SHOULD return binding for it"() {
        given:
        def classKey = ClassKey.of(SuperclassClass, sampleNamedQualifierName)

        and:
        classInstantiator.instantiateObjectOf(SubclassClass) >> new SubclassClass("", "")

        and:
        registry.bind(classKey)
                .to(SubclassClass)
                .in(Singleton)

        when:
        def binding = registry.getBindingFor(classKey).get()

        then:
        binding.getClassKey() == classKey
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Singleton
    }

    def "getBindingFor() WHEN given a class key which is not registered SHOULD return empty optional"() {
        expect:
        registry.getBindingFor(ClassKey.of(SuperclassClass)).isEmpty()
    }

    def "getBindingFor() WHEN given a class which key is registered SHOULD return binding for it"() {
        given:
        classInstantiator.instantiateObjectOf(SubclassClass) >> new SubclassClass("", "")

        and:
        registry.bind(SuperclassClass)
                .to(SubclassClass)
                .in(Singleton)

        when:
        def binding = registry.getBindingFor(SuperclassClass).get()

        then:
        binding.getClassKey() == ClassKey.of(SuperclassClass)
        binding.getProvider().get() instanceof SubclassClass
        binding.getScope() == Singleton
    }

    def "getBindingFor() WHEN given a class which key is not registered SHOULD return empty optional"() {
        expect:
        registry.getBindingFor(SuperclassClass).isEmpty()
    }

    def "isRegistered() WHEN given a class key SHOULD check if it's registered"() {
        given:
        def classKey = ClassKey.of(SuperclassClass)

        when:
        registry.bind(classKey)
                .to(SubclassClass)
                .unscoped()

        then:
        registry.isRegistered(classKey)
        !registry.isRegistered(ClassKey.of(SubclassClass))
    }

    def "isRegistered() WHEN given a class SHOULD check if it's registered"() {
        when:
        registry.bind(SuperclassClass)
                .to(SubclassClass)
                .unscoped()

        then:
        registry.isRegistered(SuperclassClass)
        !registry.isRegistered(SubclassClass)
    }

    def "registerBinding() WHEN binding is given SHOULD register it"() {
        given:
        def classKey = ClassKey.of(SuperclassClass, sampleNamedQualifierName)
        def binding = GenericBinding.of(classKey, () -> new SubclassClass("", ""), Singleton)

        when:
        registry.registerBinding(binding)

        then:
        registry.isRegistered(classKey)
    }
}
