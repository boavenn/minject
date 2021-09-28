package com.github.boavenn.minject.instantiation.generic


import com.github.boavenn.minject.instantiation.generic.GenericInjectableMethodsResolver
import spock.lang.Specification

class GenericInjectableMethodsResolverSpec extends Specification {
    def resolver = GenericInjectableMethodsResolver.create()

    def "findInjectableMethodsIn() WHEN given class has no injectable methods SHOULD return empty list"() {
        when:
        def methods = resolver.findInjectableMethodsIn(NoInjectableMethodsClass)

        then:
        methods.isEmpty()
    }

    def "findInjectableMethodsIn() WHEN given class has injectable methods SHOULD return them"() {
        when:
        def methods = resolver.findInjectableMethodsIn(InjectableMethodsClass)

        then:
        methods.size() == 2
        methods.contains(InjectableMethodsClass.getDeclaredMethod("method1"))
        methods.contains(InjectableMethodsClass.getDeclaredMethod("method2", String))
    }

    def "findInjectableMethodsIn() WHEN given class has injectable abstract methods SHOULD not return them"() {
        when:
        def methods = resolver.findInjectableMethodsIn(InjectableAbstractMethodsClass)

        then:
        methods.size() == 1
        methods.contains(InjectableAbstractMethodsClass.getDeclaredMethod("method1"))
    }
}
