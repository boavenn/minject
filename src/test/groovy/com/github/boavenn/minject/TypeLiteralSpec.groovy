package com.github.boavenn.minject

import spock.lang.Specification

import javax.inject.Provider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.function.Predicate

class TypeLiteralSpec extends Specification {
    private Provider<String> oneLevelDeepGenericField
    private Provider<List<String>> twoLevelsDeepGenericField
    private Provider<Map<Integer, List<String>>> threeLevelsDeepGenericField

    def "TypeLiteral() WHEN used to create anonymous class SHOULD create correct type literal from its declaration"() {
        when:
        def typeLiteralFromAnonymousClass = new TypeLiteral<Provider<String>>() {}

        then:
        typeLiteralFromAnonymousClass.getRawType() == Provider
        assertThatTypeIsParameterized(typeLiteralFromAnonymousClass.getType(), Provider, typeArgs -> typeArgs == [String] as Type[])
    }

    def "TypeLiteral() WHEN created with anonymous class without type args SHOULD throw an exception"() {
        when:
        new TypeLiteral() {}

        then:
        thrown(RuntimeException)
    }

    def "of() WHEN given a class SHOULD create correct type literal for it"() {
        when:
        def typeLiteral = TypeLiteral.of(cls)

        then:
        typeLiteral.getRawType() == rawType
        typeLiteral.getType() instanceof Class<?>
        typeLiteral.getType() == type

        where:
        cls                        | rawType | type
        String                     | String  | String
        List<String>               | List    | List
        Map<String, Integer>       | Map     | Map
        Map<String, List<Integer>> | Map     | Map
    }

    def "of() WHEN given a type with one level deep generics SHOULD create correct type literal for it"() {
        given:
        def field = TypeLiteralSpec.getDeclaredField("oneLevelDeepGenericField")

        when:
        def typeLiteral = TypeLiteral.of(field.getGenericType())

        then:
        typeLiteral.getRawType() == Provider
        assertThatTypeIsParameterized(typeLiteral.getType(), Provider, typeArgs -> typeArgs == [String] as Type[])
    }

    def "of() WHEN given a type with two level deep generics SHOULD create correct type literal for it"() {
        given:
        def field = TypeLiteralSpec.getDeclaredField("twoLevelsDeepGenericField")

        when:
        def typeLiteral = TypeLiteral.of(field.getGenericType())

        then:
        typeLiteral.getRawType() == Provider
        assertThatTypeIsParameterized(typeLiteral.getType(), Provider, typeArgs -> {
            typeArgs.size() == 1
            def type = typeArgs[0]
            assertThatTypeIsParameterized(type, List, listTypeArgs -> listTypeArgs == [String] as Type[])
        })
    }

    def "of() WHEN given a type with three level deep generics SHOULD create correct type literal for it"() {
        given:
        def field = TypeLiteralSpec.getDeclaredField("threeLevelsDeepGenericField")

        when:
        def typeLiteral = TypeLiteral.of(field.getGenericType())

        then:
        typeLiteral.getRawType() == Provider
        assertThatTypeIsParameterized(typeLiteral.getType(), Provider, typeArgs -> {
            typeArgs.size() == 1
            def type = typeArgs[0]
            assertThatTypeIsParameterized(type, Map, mapTypeArgs -> {
                mapTypeArgs.size() == 2
                def keyType = mapTypeArgs[0]
                keyType == Integer
                def valueType = mapTypeArgs[1]
                assertThatTypeIsParameterized(valueType, List, listTypeArgs -> listTypeArgs == [String] as Type[])
            })
        })
    }

    def "asProviderType() WHEN called on type literal SHOULD return provider of the type specified by this type literal"() {
        given:
        def typeLiteral = TypeLiteral.of(String)

        when:
        def providerTypeLiteral = typeLiteral.asProviderType()
        def expectedTypeLiteral = new TypeLiteral<Provider<String>>() {}

        then:
        providerTypeLiteral.getRawType() == Provider
        assertThatTypeIsParameterized(providerTypeLiteral.getType(), Provider, typeArgs -> typeArgs == [String] as Type[])

        and:
        providerTypeLiteral == expectedTypeLiteral
        expectedTypeLiteral == providerTypeLiteral
        providerTypeLiteral.hashCode() == expectedTypeLiteral.hashCode()
    }

    def "getInnerType() WHEN called on a type literal of parameterized type with single type arg SHOULD return this inner type literal"() {
        given:
        def typeLiteral = new TypeLiteral<Provider<List<String>>>() {}

        when:
        def innerTypeLiteral = typeLiteral.getInnerType()

        then:
        innerTypeLiteral.getRawType() == List
        assertThatTypeIsParameterized(innerTypeLiteral.getType(), List, typeArgs -> typeArgs == [String] as Type[])
        innerTypeLiteral == new TypeLiteral<List<String>>() {}
    }

    def "getInnerType() WHEN called on a type literal of parameterized type with multiple type args SHOULD throw an exception"() {
        given:
        def typeLiteral = new TypeLiteral<Map<Integer, String>>() {}

        when:
        typeLiteral.getInnerType()

        then:
        thrown(RuntimeException)
    }

    def "getInnerType() WHEN called on a type literal of nonparameterized type SHOULD throw an exception"() {
        given:
        def typeLiteral = new TypeLiteral<String>() {}

        when:
        typeLiteral.getInnerType()

        then:
        thrown(RuntimeException)
    }

    private static def assertThatTypeIsParameterized(Type type, Class<?> rawType, Predicate<Type[]> typeArgsCondition) {
        type instanceof ParameterizedType
                && type.getRawType() == rawType
                && typeArgsCondition.test(type.getActualTypeArguments())
    }
}
