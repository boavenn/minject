package com.github.boavenn.minject.configuration.generic


import com.github.boavenn.minject.binding.BindingProviderBuilder
import com.github.boavenn.minject.binding.BindingScopeBuilder
import com.github.boavenn.minject.configuration.Binder
import com.github.boavenn.minject.injector.Injector
import com.github.boavenn.minject.scope.Unscoped
import spock.lang.Specification

import javax.inject.Singleton

class GenericModuleProcessorSpec extends Specification {
    def scopeBuilder = Mock(BindingScopeBuilder)
    def providerBuilder = Mock(BindingProviderBuilder)
    def binder = Mock(Binder)
    def injector = Mock(Injector)

    def moduleProcessor = new GenericModuleProcessor()

    def "process() WHEN processing module SHOULD bind all @Provides methods correctly"() {
        given:
        def sampleModule = new SampleModule()

        when:
        moduleProcessor.process(sampleModule, binder, injector)

        then:
        4 * binder.bind(_) >> providerBuilder
        4 * providerBuilder.toProvider(_) >> scopeBuilder
        2 * scopeBuilder.in(Singleton)
        2 * scopeBuilder.in(Unscoped)
    }
}
