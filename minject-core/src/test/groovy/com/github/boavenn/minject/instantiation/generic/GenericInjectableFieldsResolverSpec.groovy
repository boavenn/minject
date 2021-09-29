package com.github.boavenn.minject.instantiation.generic


import spock.lang.Specification

class GenericInjectableFieldsResolverSpec extends Specification {
    def resolver = GenericInjectableFieldsResolver.create()

    def "findInjectableFieldsIn() WHEN given class has no injectable fields SHOULD return empty list"() {
        when:
        def fields = resolver.findInjectableFieldsIn(NoInjectableMethodsClass)

        then:
        fields.isEmpty()
    }

    def "findInjectableFieldsIn() WHEN given class injectable fields SHOULD return them"() {
        when:
        def fields = resolver.findInjectableFieldsIn(InjectableFieldsClass)

        then:
        fields.size() == 2
        fields.contains(InjectableFieldsClass.getDeclaredField("field1"))
        fields.contains(InjectableFieldsClass.getDeclaredField("field2"))
    }

    def "findInjectableFieldsIn() WHEN given class injectable final fields SHOULD not return them"() {
        when:
        def fields = resolver.findInjectableFieldsIn(InjectableFinalFieldsClass)

        then:
        fields.size() == 1
        fields.contains(InjectableFinalFieldsClass.getDeclaredField("field1"))
    }
}
