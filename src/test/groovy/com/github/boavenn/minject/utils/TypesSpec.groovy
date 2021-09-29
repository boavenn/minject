package com.github.boavenn.minject.utils

import com.github.boavenn.minject.exceptions.TypeException
import com.github.boavenn.minject.TypeLiteral
import spock.lang.Specification

import javax.inject.Provider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.function.Predicate

class TypesSpec extends Specification {
    String nonParameterizedField
    Map<Integer, Provider<String>> parameterizedField

    def nonParameterizedFieldName = "nonParameterizedField"
    def parameterizedFieldName = "parameterizedField"

    private static class GenericSuperclass<T> {}

    def "getRawTypeOf() WHEN given nonparameterized type SHOULD return it"() {
        given:
        def type = TypesSpec.class.getDeclaredField(nonParameterizedFieldName).getGenericType()

        when:
        def rawType = Types.getRawTypeOf(type)

        then:
        rawType == String
    }

    def "getRawTypeOf() WHEN given parameterized type SHOULD return its raw type"() {
        given:
        def type = TypesSpec.class.getDeclaredField(parameterizedFieldName).getGenericType()

        when:
        def rawType = Types.getRawTypeOf(type)

        then:
        rawType == Map
    }

    def "getSuperclassTypesOf() WHEN superclass of a given class is not parameterized SHOULD return empty list"() {
        given:
        def subclassObject = new GenericSuperclass() {}
        def subclassClass = subclassObject.class

        when:
        def superclassTypes = Types.getSuperclassTypesOf(subclassClass)

        then:
        superclassTypes.isEmpty()
    }

    def "getSuperclassTypesOf() WHEN superclass of a given class is parameterized SHOULD return its types"() {
        given:
        def subclassObject = new GenericSuperclass<Map<Integer, Provider<String>>>() {}
        def subclassClass = subclassObject.class

        when:
        def superclassTypes = Types.getSuperclassTypesOf(subclassClass)

        then:
        superclassTypes.size() == 1
        assertThatTypeIsParameterized(superclassTypes.get(0), Map, mapTypeArgs -> {
            mapTypeArgs.length == 2
            mapTypeArgs[0] == Integer
            assertThatTypeIsParameterized(mapTypeArgs[1], Provider, providerTypeArgs -> providerTypeArgs == [String] as Type[])
        })
    }

    def "getTypeLiteralsOfNestedTypesIn() WHEN given a type literal of parameterized type SHOULD return its nested types"() {
        given:
        def typeLiteral = new TypeLiteral<Map<Integer, Provider<String>>>() {}

        when:
        def nestedTypeLiterals = Types.getTypeLiteralsOfNestedTypesIn(typeLiteral)

        then:
        nestedTypeLiterals.size() == 2
        nestedTypeLiterals.get(0) == TypeLiteral.of(Integer)
        nestedTypeLiterals.get(1) == new TypeLiteral<Provider<String>>() {}
    }

    def "getTypeLiteralsOfNestedTypesIn() WHEN given a type literal of nonparameterized type SHOULD throw an exception"() {
        given:
        def typeLiteral = new TypeLiteral<String>() {}

        when:
        Types.getTypeLiteralsOfNestedTypesIn(typeLiteral)

        then:
        TypeException ex = thrown()
        ex.getMessage() == TypeException.nestedTypeOfNonparameterizedType().getMessage()
    }

    private static def assertThatTypeIsParameterized(Type type, Class<?> rawType, Predicate<Type[]> typeArgsCondition) {
        type instanceof ParameterizedType
                && type.getRawType() == rawType
                && typeArgsCondition.test(type.getActualTypeArguments())
    }
}
