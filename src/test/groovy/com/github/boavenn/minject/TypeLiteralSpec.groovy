package com.github.boavenn.minject

import spock.lang.Specification

import javax.inject.Provider
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class TypeLiteralSpec extends Specification {
    def "of() WHEN given a class SHOULD create correct type literal for it"() {
        when:
        def typeLiteral = TypeLiteral.of(cls)

        then:
        typeLiteral.getRawType() == rawType
        typeLiteral.getType() instanceof Class<?>
        typeLiteral.getType() == rawType

        where:
        cls                        | rawType
        String                     | String
        List<String>               | List
        Map<String, Integer>       | Map
        Map<String, List<Integer>> | Map
    }

    def "TypeLiteral() WHEN used to create anonymous class SHOULD create correct type literal from its declaration"() {
        when:
        def typeLiteralFromAnonymousClass = new TypeLiteral<Provider<String>>() {}

        then:
        typeLiteralFromAnonymousClass.getRawType() == Provider
        typeLiteralFromAnonymousClass.getType() instanceof ParameterizedType

        and:
        def parameterizedType = (ParameterizedType) typeLiteralFromAnonymousClass.getType()
        parameterizedType.getRawType() == Provider
        parameterizedType.getActualTypeArguments() == [String] as Type[]

        and:
        def typeLiteralDirectlyFromAClass = TypeLiteral.of(Provider<String>)
        typeLiteralFromAnonymousClass.getRawType() == typeLiteralDirectlyFromAClass.getRawType()
        typeLiteralFromAnonymousClass.getType() != typeLiteralDirectlyFromAClass.getType()
    }
}
