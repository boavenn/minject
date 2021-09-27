package com.github.boavenn.minject.instantiation.generic


import com.github.boavenn.minject.exceptions.InjectionPointException
import com.github.boavenn.minject.instantiaton.generic.GenericInjectableConstructorResolver
import spock.lang.Specification

class GenericInjectableConstructorResolverSpec extends Specification {
    def resolver = GenericInjectableConstructorResolver.create()

    def "findInjectableConstructorIn() WHEN given class has only default constructor SHOULD return it"() {
        when:
        def expected = DefaultConstructorClass.getDeclaredConstructor()

        then:
        resolver.findInjectableConstructorIn(DefaultConstructorClass) == expected
    }

    def "findInjectableConstructorIn() WHEN given class has only one constructor SHOULD return it"() {
        when:
        def expected = SingleConstructorClass.getDeclaredConstructor()

        then:
        resolver.findInjectableConstructorIn(SingleConstructorClass) == expected
    }

    def "findInjectableConstructorIn() WHEN given class has one annotated constructor SHOULD return it"() {
        when:
        def expected = SingleAnnotatedConstructorClass.getDeclaredConstructor()

        then:
        resolver.findInjectableConstructorIn(SingleAnnotatedConstructorClass) == expected
    }

    def "findInjectableConstructorIn() WHEN given class has multiple annotated constructors SHOULD throw an exception"() {
        when:
        resolver.findInjectableConstructorIn(MultipleAnnotatedConstructorsClass)

        then:
        InjectionPointException ex = thrown()
        ex.getMessage() == InjectionPointException.MULTIPLE_CONSTR_FOUND_MSG
    }

    def "findInjectableConstructorIn() WHEN given class has multiple unannotated constructors SHOULD throw an exception"() {
        when:
        resolver.findInjectableConstructorIn(MultipleUnannotatedConstructorsClass)

        then:
        InjectionPointException ex = thrown()
        ex.getMessage() == InjectionPointException.CONSTR_NOT_FOUND_MSG
    }
}
