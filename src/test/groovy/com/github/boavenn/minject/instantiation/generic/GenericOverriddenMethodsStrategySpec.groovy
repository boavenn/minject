package com.github.boavenn.minject.instantiation.generic

import com.github.boavenn.minject.instantiation.generic.somepackage.SubclassOverrideAnotherPackageClass
import com.github.boavenn.minject.instantiaton.generic.GenericOverriddenMethodsStrategy
import spock.lang.Specification

class GenericOverriddenMethodsStrategySpec extends Specification {
    def ovverridenMethodsStrategy = GenericOverriddenMethodsStrategy.create()

    def publicMethodName = "publicMethod"
    def packagePrivateMethodName = "packagePrivateMethod"
    def protectedMethodName = "protectedMethod"
    def privateMethodName = "privateMethod"

    def "removeOverriddenMethods() WHEN given methods of superclass and subclass from same package SHOULD return correct methods"() {
        given:
        def subclassMethods = [
                SubclassOverrideClass.getDeclaredMethod(publicMethodName, String),
                SubclassOverrideClass.getDeclaredMethod(packagePrivateMethodName, String),
                SubclassOverrideClass.getDeclaredMethod(protectedMethodName, String),
                SubclassOverrideClass.getDeclaredMethod(privateMethodName, String)
        ]
        def superclassMethods = [
                SuperclassOverrideClass.getDeclaredMethod(publicMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(packagePrivateMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(protectedMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(privateMethodName, String)
        ]

        when:
        def filteredSuperclassMethods = ovverridenMethodsStrategy.removeOverriddenMethods(subclassMethods, superclassMethods)

        then:
        filteredSuperclassMethods.size() == 1
        filteredSuperclassMethods.contains(SuperclassOverrideClass.getDeclaredMethod(privateMethodName, String))
    }

    def "removeOverriddenMethods() WHEN given methods of superclass and subclass from another package SHOULD return correct methods"() {
        given:
        def subclassMethods = [
                SubclassOverrideAnotherPackageClass.getDeclaredMethod(publicMethodName, String),
                SubclassOverrideAnotherPackageClass.getDeclaredMethod(packagePrivateMethodName, String),
                SubclassOverrideAnotherPackageClass.getDeclaredMethod(protectedMethodName, String),
                SubclassOverrideAnotherPackageClass.getDeclaredMethod(privateMethodName, String)
        ]
        def superclassMethods = [
                SuperclassOverrideClass.getDeclaredMethod(publicMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(packagePrivateMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(protectedMethodName, String),
                SuperclassOverrideClass.getDeclaredMethod(privateMethodName, String)
        ]

        when:
        def filteredSuperclassMethods = ovverridenMethodsStrategy.removeOverriddenMethods(subclassMethods, superclassMethods)

        then:
        filteredSuperclassMethods.size() == 2
        filteredSuperclassMethods.contains(SuperclassOverrideClass.getDeclaredMethod(privateMethodName, String))
        filteredSuperclassMethods.contains(SuperclassOverrideClass.getDeclaredMethod(packagePrivateMethodName, String))
    }
}
