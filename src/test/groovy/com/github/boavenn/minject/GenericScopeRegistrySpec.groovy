package com.github.boavenn.minject

import spock.lang.Specification

import javax.inject.Singleton

class GenericScopeRegistrySpec extends Specification {
    def registry = GenericScopeRegistry.empty()
    def sampleScopeHandler = Mock(ScopeHandler)

    def "registerScope() WHEN scope and its handler are given SHOULD register them"() {
        when:
        registry.registerScope(Singleton, sampleScopeHandler)

        then:
        registry.isRegistered(Singleton)
    }

    def "getScopeHandlerFor() WHEN given a scope which is registered SHOULD return its handler"() {
        given:
        registry.registerScope(Singleton, sampleScopeHandler)

        when:
        def scopeHandler = registry.getScopeHandlerFor(Singleton).get()

        then:
        scopeHandler === sampleScopeHandler
    }

    def "getScopeHandlerFor() WHEN given a scope which is not registered SHOULD return empty optional"() {
        expect:
        registry.getScopeHandlerFor(Singleton).isEmpty()
    }

    def "isRegistered() WHEN scope is given SHOULD check if it's registered"() {
        when:
        registry.registerScope(Singleton, sampleScopeHandler)

        then:
        registry.isRegistered(Singleton)
        !registry.isRegistered(Unscoped)
    }
}
