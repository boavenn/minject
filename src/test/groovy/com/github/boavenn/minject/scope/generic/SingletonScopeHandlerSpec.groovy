package com.github.boavenn.minject.scope.generic

import com.github.boavenn.minject.binding.generic.SuperclassClass
import com.github.boavenn.minject.injector.ClassKey
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler
import org.spockframework.util.Assert
import spock.lang.Specification

import javax.inject.Provider

class SingletonScopeHandlerSpec extends Specification {
    def handler = SingletonScopeHandler.empty()

    def "scopeProvider() WHEN given a class key and unscoped provider SHOULD scope it so same instance is returned everytime"() {
        given:
        def classKey = ClassKey.of(SampleClass)
        def unscopedProvider = Mock(Provider<SampleClass>) {
            1 * get() >> { return new SampleClass() }
        }

        when:
        def scopedProvider = handler.scopeProvider(classKey, unscopedProvider)
        def instance1 = scopedProvider.get()
        def instance2 = scopedProvider.get()

        then:
        Assert.notNull(instance1)
        Assert.notNull(instance2)
        instance1 === instance2
    }
}
