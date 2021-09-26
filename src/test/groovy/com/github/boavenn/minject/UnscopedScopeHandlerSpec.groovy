package com.github.boavenn.minject

import org.spockframework.util.Assert
import spock.lang.Specification

import javax.inject.Provider

class UnscopedScopeHandlerSpec extends Specification {
    def handler = UnscopedScopeHandler.empty()

    def "scopeProvider() WHEN given a class key and unscoped provider SHOULD scope it so new instance is returned everytime"() {
        given:
        def classKey = ClassKey.of(SuperclassClass)
        def unscopedProvider = Mock(Provider<SuperclassClass>) {
            2 * get() >> { return new SuperclassClass("") }
        }

        when:
        def scopedProvider = handler.scopeProvider(classKey, unscopedProvider)
        def instance1 = scopedProvider.get()
        def instance2 = scopedProvider.get()

        then:
        Assert.notNull(instance1)
        Assert.notNull(instance2)
        instance1 !== instance2
    }
}
