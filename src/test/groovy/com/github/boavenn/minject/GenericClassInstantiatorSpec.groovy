package com.github.boavenn.minject

import spock.lang.Specification

class GenericClassInstantiatorSpec extends Specification {
    def injector = Mock(Injector)
    def instantiator = GenericClassInstantiator.using(injector).build()

    def "instantiateObjectOf() WHEN given class has no superclass SHOULD instantiate it properly"() {
        given:
        def fieldStringInstance = "superclassFieldString"
        def constructorStringInstance = "superclassConstructorParamString"
        def methodParamStringInstance = "superclassMethodParamString"

        and:
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.fieldName)) >> fieldStringInstance
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.constructorParamName)) >> constructorStringInstance
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.methodParamName)) >> methodParamStringInstance

        when:
        def instance = instantiator.instantiateObjectOf(SuperclassClass)

        then:
        instance.getSuperclassField() == fieldStringInstance
        instance.getSuperclassConstructorParam() == constructorStringInstance
        instance.getSuperclassMethodParam() == methodParamStringInstance
    }

    def "instantiateObjectOf() WHEN given class has superclass SHOULD instantiate it properly"() {
        given:
        def superclassFieldStringInstance = "superclassFieldString"
        def superclassConstructorStringInstance = "superclassConstructorParamString"
        def superclassMethodParamStringInstance = "superclassMethodParamString"

        def subclassFieldStringInstance = "subclassFieldString"
        def subclassConstructorStringInstance = "subclassConstructorParamString"
        def subclassMethodParamStringInstance = "subclassMethodParamString"

        and:
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.fieldName)) >> superclassFieldStringInstance
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.constructorParamName)) >> superclassConstructorStringInstance
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.methodParamName)) >> superclassMethodParamStringInstance

        injector.getInstanceOf(ClassKey.of(String, SubclassClass.fieldName)) >> subclassFieldStringInstance
        injector.getInstanceOf(ClassKey.of(String, SubclassClass.constructorParamName)) >> subclassConstructorStringInstance
        injector.getInstanceOf(ClassKey.of(String, SubclassClass.methodParamName)) >> subclassMethodParamStringInstance

        when:
        def instance = instantiator.instantiateObjectOf(SubclassClass)

        then:
        instance.getSuperclassField() == superclassFieldStringInstance
        instance.getSuperclassConstructorParam() == superclassConstructorStringInstance
        instance.getSuperclassMethodParam() == superclassMethodParamStringInstance
        instance.getSubclassField() == subclassFieldStringInstance
        instance.getSubclassConstructorParam() == subclassConstructorStringInstance
        instance.getSubclassMethodParam() == subclassMethodParamStringInstance
    }

    def "instantiateObjectOf() WHEN constructor injection is impossible for whatever reason SHOULD throw an exception"() {
        given:
        // Return value of type different than the constructor parameter type so an exception is thrown
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.constructorParamName)) >> 123

        when:
        instantiator.instantiateObjectOf(SuperclassClass)

        then:
        InjectionException ex = thrown()
        ex.getMessage() == InjectionException.INJECTABLE_CONSTRUCTOR_INVOCATION_MSG
    }

    def "instantiateObjectOf() WHEN field injection is impossible for whatever reason SHOULD throw an exception"() {
        given:
        def constructorStringInstance = "superclassConstructorParamString"

        and:
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.constructorParamName)) >> constructorStringInstance
        // Return value of type different than the constructor parameter type so an exception is thrown
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.fieldName)) >> 123

        when:
        instantiator.instantiateObjectOf(SuperclassClass)

        then:
        InjectionException ex = thrown()
        ex.getMessage() == InjectionException.INJECTABLE_FIELD_SET_MSG
    }

    def "instantiateObjectOf() WHEN method injection is impossible for whatever reason SHOULD throw an exception"() {
        given:
        def fieldStringInstance = "superclassFieldString"
        def constructorStringInstance = "superclassConstructorParamString"

        and:
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.fieldName)) >> fieldStringInstance
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.constructorParamName)) >> constructorStringInstance
        // Return value of type different than the constructor parameter type so an exception is thrown
        injector.getInstanceOf(ClassKey.of(String, SuperclassClass.methodParamName)) >> 123

        when:
        instantiator.instantiateObjectOf(SuperclassClass)

        then:
        InjectionException ex = thrown()
        ex.getMessage() == InjectionException.INJECTABLE_METHOD_INVOCATION_MSG
    }
}
