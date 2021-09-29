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
        def stringAKey = ClassKey.of(String, SampleModuleA.propertyName)
        def stringBKey = ClassKey.of(String, SampleModuleB.propertyName)
        def providesAKey = ClassKey.of(String, SampleModuleA.providedStringName)
        def providesBKey = ClassKey.of(String, SampleModuleB.providedStringName)

        when:
        def injector = injectorFactory.addModules(List.of(sampleModule)).create()
        def stringA = injector.getInstanceOf(stringAKey)
        def stringB = injector.getInstanceOf(stringBKey)
        def integer = injector.getInstanceOf(Integer)

        then:
        stringA == SampleModuleA.propertyValue
        stringB == SampleModuleB.propertyValue
        integer == SampleModuleA.integerValue

        injector.getInstanceOf(providesAKey) == stringA + stringB
        injector.getInstanceOf(providesAKey) === injector.getInstanceOf(providesAKey)
        injector.getInstanceOf(providesBKey) == stringB + integer
        injector.getInstanceOf(providesBKey) !== injector.getInstanceOf(providesBKey)
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
        def injector = injectorFactory.addModules(List.of(sampleCircularModuleA)).create()

        then:
        injector.getInstanceOf(Injector) == injector
    }
}
