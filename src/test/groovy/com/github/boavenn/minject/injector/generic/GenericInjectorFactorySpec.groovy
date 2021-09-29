package com.github.boavenn.minject.injector.generic

import com.github.boavenn.minject.ClassKey
import com.github.boavenn.minject.configuration.Binder
import com.github.boavenn.minject.configuration.ConfigurationModule
import com.github.boavenn.minject.injector.Injector
import spock.lang.Specification

class GenericInjectorFactorySpec extends Specification {
    def injectorFactory = new GenericInjectorFactory()

    def "create() SHOULD create generic injector with default configuration"() {
        when:
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(Injector) == injector
        injector.getInstanceOf(UnqualifiedUnscopedClass) !== injector.getInstanceOf(UnqualifiedUnscopedClass)
        injector.getInstanceOf(UnqualifiedSingletonClass) === injector.getInstanceOf(UnqualifiedSingletonClass)
    }

    def "create() WHEN given some modules SHOULD register their configuration to injector"() {
        given:
        def sampleModule = new SampleModuleA()

        when:
        injectorFactory.addModules(List.of(sampleModule))
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(ClassKey.of(String, SampleModuleA.propertyName)) == SampleModuleA.propertyValue
        injector.getInstanceOf(Integer) == SampleModuleA.integerValue
    }

    def "create() WHEN one module installs the other SHOULD create generic injector with their configuration"() {
        given:
        def sampleModule = new SampleModuleA()

        when:
        injectorFactory.addModules(List.of(sampleModule))
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(ClassKey.of(String, SampleModuleA.propertyName)) == SampleModuleA.propertyValue
        injector.getInstanceOf(Integer) == SampleModuleA.integerValue
        injector.getInstanceOf(ClassKey.of(String, SampleModuleB.propertyName)) == SampleModuleB.propertyValue
    }

    def "create() WHEN there is circular reference between modules SHOULD not register same module two times"() {
        given:
        def sampleCircularModuleA = Mock(ConfigurationModule)
        def sampleCircularModuleB = Mock(ConfigurationModule)

        and:
        1 * sampleCircularModuleA.configure(_ as Binder) >> { Binder binder ->
            binder.install(sampleCircularModuleB)
        }
        1 * sampleCircularModuleB.configure(_ as Binder) >> { Binder binder ->
            binder.install(sampleCircularModuleA)
        }

        when:
        injectorFactory.addModules(List.of(sampleCircularModuleA))
        def injector = injectorFactory.create()

        then:
        injector.getInstanceOf(Injector) == injector
    }
}
