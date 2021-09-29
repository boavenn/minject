package com.github.boavenn.minject


import com.github.boavenn.minject.exceptions.ClassKeyCreationException
import spock.lang.Specification

import javax.inject.Named
import javax.inject.Provider

class ClassKeySpec extends Specification {
    def namedQualifierAnnotation = SampleClassWithNamedQualifier.getAnnotation(Named)
    def customQualifierAnnotation = SampleClassWithCustomQualifier.getAnnotation(CustomQualifier)

    def sampleNamedQualifierValue = SampleClassWithNamedQualifier.sampleNamedQualifierValue
    def sampleFieldName = "sampleField"
    def sampleProviderFieldName = "sampleProviderField"
    def sampleMethodName = "sampleMethod"
    def sampleProviderMethodName = "sampleProviderMethod"
    def sampleVoidMethodName = "sampleVoidMethod"

    def "of() WHEN given type literal only SHOULD return correct key"() {
        when:
        def key = ClassKey.of(new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {})

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}
        key.getName() == null
        key.getQualifier() == null
    }

    def "of() WHEN given type literal and explicit name SHOULD return correct key"() {
        when:
        def key = ClassKey.of(new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}, sampleNamedQualifierValue)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
    }

    def "of() WHEN given type literal and named qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}, namedQualifierAnnotation)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
    }

    def "of() WHEN given type literal and custom qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}, customQualifierAnnotation)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<SampleClassWithNoQualifier>>() {}
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
    }

    def "of() WHEN given class only SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNoQualifier)
        key.getName() == null
        key.getQualifier() == null
    }

    def "of() WHEN given class and explicit name SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, sampleNamedQualifierValue)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNoQualifier)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
    }

    def "of() WHEN given class and named qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, namedQualifierAnnotation)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNoQualifier)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
    }

    def "of() WHEN given class and custom qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, customQualifierAnnotation)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNoQualifier)
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
    }

    def "from() WHEN given class with named qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithNamedQualifier)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNamedQualifier)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(SampleClassWithNamedQualifier, sampleNamedQualifierValue)
    }

    def "from() WHEN given class with custom qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithCustomQualifier)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithCustomQualifier)
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(SampleClassWithCustomQualifier, customQualifierAnnotation)
    }

    def "from() WHEN given class with no qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithNoQualifier)

        then:
        key.getTypeLiteral() == TypeLiteral.of(SampleClassWithNoQualifier)
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(SampleClassWithNoQualifier)
    }

    def "from() WHEN given field with named qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithNamedQualifier.getDeclaredField(sampleFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given provider field with named qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithNamedQualifier.getDeclaredField(sampleProviderFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, sampleNamedQualifierValue)
    }

    def "from() WHEN given field with custom qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithCustomQualifier.getDeclaredField(sampleFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given provider field with custom qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithCustomQualifier.getDeclaredField(sampleProviderFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, customQualifierAnnotation)
    }

    def "from() WHEN given field with no qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithNoQualifier.getDeclaredField(sampleFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }

    def "from() WHEN given provider field with no qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithNoQualifier.getDeclaredField(sampleProviderFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {})
    }

    def "from() WHEN given method with void return type SHOULD throw an exception"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleVoidMethodName)

        when:
        ClassKey.from(method)

        then:
        ClassKeyCreationException ex = thrown()
        ex.getMessage() == ClassKeyCreationException.methodOfVoidReturnType().getMessage()
    }

    def "from() WHEN given method with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given provider method with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleProviderMethodName, Provider)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, sampleNamedQualifierValue)
    }

    def "from() WHEN given method with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given provider method with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleProviderMethodName, Provider)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, customQualifierAnnotation)
    }

    def "from() WHEN given method with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }

    def "from() WHEN given provider method with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleProviderMethodName, Provider)

        when:
        def key = ClassKey.from(method)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {})
    }

    def "from() WHEN given param with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given provider param with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleProviderMethodName, Provider)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, sampleNamedQualifierValue)
    }

    def "from() WHEN given param with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given provider param with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleProviderMethodName, Provider<String>)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {}, customQualifierAnnotation)
    }

    def "from() WHEN given param with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == TypeLiteral.of(String)
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }

    def "from() WHEN given provider param with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleProviderMethodName, Provider<String>)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getTypeLiteral() == new TypeLiteral<Provider<String>>() {}
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(new TypeLiteral<Provider<String>>() {})
    }

    def "toProviderKey() SHOULD return class key of provider providing instances of key previous type"() {
        given:
        def key = ClassKey.of(String)

        when:
        def providerKey = key.toProviderKey()
        def keyTypeLiteral = providerKey.getTypeLiteral()
        def expectedTypeLiteral = new TypeLiteral<Provider<String>>() {}
        def expectedKey = ClassKey.of(expectedTypeLiteral)

        then:
        providerKey.getName() == null
        providerKey.getQualifier() == null

        and:
        providerKey == expectedKey
        expectedKey == providerKey
        providerKey.hashCode() == expectedKey.hashCode()

        and:
        keyTypeLiteral == expectedTypeLiteral
        expectedTypeLiteral == keyTypeLiteral
        keyTypeLiteral.hashCode() == expectedTypeLiteral.hashCode()
    }

    def "with() WHEN given new type literal SHOULD return same key but with new type literal"() {
        given:
        def key = ClassKey.of(String, sampleNamedQualifierValue)

        when:
        def newKey = key.with(new TypeLiteral<List<Integer>>() {})

        then:
        newKey.getTypeLiteral() == new TypeLiteral<List<Integer>>() {}
        newKey.getName() == sampleNamedQualifierValue
        newKey.getQualifier() == null
    }
}
