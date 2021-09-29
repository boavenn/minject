package com.github.boavenn.minject.injector.generic

import com.github.boavenn.minject.ClassKey
import com.github.boavenn.minject.injector.Injector
import spock.lang.Specification

class GenericInjectorFactorySpec extends Specification {
    def injectorFactory = new GenericInjectorFactory()

    def "addModules() WHEN given some modules SHOULD register their configuration to injector"() {
        given:
        def sampleModule = new SampleModule()

        when:
        injectorFactory.addModules(List.of(sampleModule))
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(ClassKey.of(String, SampleModule.propertyName)) == SampleModule.propertyValue
        injector.getInstanceOf(Integer) == SampleModule.integerValue
    }

    def "create() SHOULD create generic injector with default configuration"() {
        when:
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(Injector) == injector
        injector.getInstanceOf(UnqualifiedUnscopedClass) !== injector.getInstanceOf(UnqualifiedUnscopedClass)
        injector.getInstanceOf(UnqualifiedSingletonClass) === injector.getInstanceOf(UnqualifiedSingletonClass)
    }
}
