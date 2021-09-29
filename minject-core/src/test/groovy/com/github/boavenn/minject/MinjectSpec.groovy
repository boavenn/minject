package com.github.boavenn.minject

import com.github.boavenn.minject.injector.Injector
import spock.lang.Specification

class MinjectSpec extends Specification {
    def "createInjector() SHOULD create correct default injector"() {
        when:
        def injector = Minject.createInjector()

        then:
        injector.getInstanceOf(Injector) == injector
        injector.getInstanceOf(Injector) === injector.getInstanceOf(Injector)
    }

    def "createInjectorWith() WHEN given some modules SHOULD create injector correctly"() {
        given:
        def propertyKey = ClassKey.of(String, SampleModule.propertyName)
        def providesKey = ClassKey.of(String, SampleModule.providedStringName)
        def sampleModule = new SampleModule()

        when:
        def injector = Minject.createInjectorWith(sampleModule)

        then:
        injector.getInstanceOf(Injector) == injector
        injector.getInstanceOf(Injector) === injector.getInstanceOf(Injector)
        injector.getInstanceOf(propertyKey) == SampleModule.propertyValue
        injector.getInstanceOf(providesKey) == SampleModule.providedStringValue
        injector.getInstanceOf(Integer) == SampleModule.integerValue
    }

    def "createInjectorWith() WHEN given some modules and module processors SHOULD create injector correctly"() {
        given:
        def propertyKey = ClassKey.of(String, SampleModule.propertyName)
        def providesKey = ClassKey.of(String, SampleModule.providedStringName)
        def sampleModule = new SampleModule()
        def sampleModuleProcessor = new SampleModuleProcessor()

        when:
        def injector = Minject.createInjectorWith(List.of(sampleModule), List.of(sampleModuleProcessor))

        then:
        injector.getInstanceOf(Injector) == injector
        injector.getInstanceOf(Injector) === injector.getInstanceOf(Injector)
        injector.getInstanceOf(propertyKey) == SampleModule.propertyValue
        injector.getInstanceOf(providesKey) == SampleModule.providedStringValue
        injector.getInstanceOf(Integer) == SampleModule.integerValue
        sampleModuleProcessor.getProcessedModulesCounter() == 1
    }
}
