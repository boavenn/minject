package com.github.boavenn.minject

import spock.lang.Specification

import javax.inject.Named

class ClassKeySpec extends Specification {
    def namedQualifierAnnotation = SampleClassWithNamedQualifier.getAnnotation(Named)
    def customQualifierAnnotation = SampleClassWithCustomQualifier.getAnnotation(CustomQualifier)

    def sampleNamedQualifierValue = SampleClassWithNamedQualifier.sampleNamedQualifierValue
    def sampleFieldName = "sampleField"
    def sampleMethodName = "sampleMethod"
    def sampleVoidMethodName = "sampleVoidMethod"

    def "of() WHEN given class only SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier)

        then:
        key.getIdentifiedClass() == SampleClassWithNoQualifier
        key.getName() == null
        key.getQualifier() == null
    }

    def "of() WHEN given class and explicit name SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, sampleNamedQualifierValue)

        then:
        key.getIdentifiedClass() == SampleClassWithNoQualifier
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
    }

    def "of() WHEN given class and named qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, namedQualifierAnnotation)

        then:
        key.getIdentifiedClass() == SampleClassWithNoQualifier
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(SampleClassWithNoQualifier, sampleNamedQualifierValue)
    }

    def "of() WHEN given class and custom qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.of(SampleClassWithNoQualifier, customQualifierAnnotation)

        then:
        key.getIdentifiedClass() == SampleClassWithNoQualifier
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
    }

    def "from() WHEN given class with named qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithNamedQualifier)

        then:
        key.getIdentifiedClass() == SampleClassWithNamedQualifier
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(SampleClassWithNamedQualifier, sampleNamedQualifierValue)
    }

    def "from() WHEN given class with custom qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithCustomQualifier)

        then:
        key.getIdentifiedClass() == SampleClassWithCustomQualifier
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(SampleClassWithCustomQualifier, customQualifierAnnotation)
    }

    def "from() WHEN given class with no qualifier SHOULD return correct key"() {
        when:
        def key = ClassKey.from(SampleClassWithNoQualifier)

        then:
        key.getIdentifiedClass() == SampleClassWithNoQualifier
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
        key.getIdentifiedClass() == String
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given field with custom qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithCustomQualifier.getDeclaredField(sampleFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given field with no qualifier SHOULD return correct key"() {
        given:
        def field = SampleClassWithNoQualifier.getDeclaredField(sampleFieldName)

        when:
        def key = ClassKey.from(field)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }

    def "from() WHEN given method with void return type SHOULD throw an exception"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleVoidMethodName)

        when:
        ClassKey.from(method)

        then:
        ClassKeyCreationException ex = thrown()
        ex.getMessage() == ClassKeyCreationException.VOID_METHOD_MSG
    }

    def "from() WHEN given method with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getIdentifiedClass() == String
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given method with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given method with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleMethodName, String)

        when:
        def key = ClassKey.from(method)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }

    def "from() WHEN given param with named qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNamedQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getIdentifiedClass() == String
        key.getName() == sampleNamedQualifierValue
        key.getQualifier() == null
        key == ClassKey.of(String, sampleNamedQualifierValue)
    }

    def "from() WHEN given param with custom qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithCustomQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == customQualifierAnnotation
        key == ClassKey.of(String, customQualifierAnnotation)
    }

    def "from() WHEN given param with no qualifier SHOULD return correct key"() {
        given:
        def method = SampleClassWithNoQualifier.getDeclaredMethod(sampleMethodName, String)
        def param = method.getParameters()[0]

        when:
        def key = ClassKey.from(param)

        then:
        key.getIdentifiedClass() == String
        key.getName() == null
        key.getQualifier() == null
        key == ClassKey.of(String)
    }
}
