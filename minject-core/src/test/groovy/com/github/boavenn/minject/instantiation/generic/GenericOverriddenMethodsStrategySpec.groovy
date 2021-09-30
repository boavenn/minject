package com.github.boavenn.minject.instantiation.generic

import com.github.boavenn.minject.instantiation.generic.somepackage.SubclassOverrideAnotherPackageClass
import spock.lang.Specification

import java.lang.reflect.Method

class GenericOverriddenMethodsStrategySpec extends Specification {
    def ovverridenMethodsStrategy = GenericOverriddenMethodsStrategy.create()

    def publicMethodName = "publicMethod"
    def packagePrivateMethodName = "packagePrivateMethod"
    def protectedMethodName = "protectedMethod"
    def privateMethodName = "privateMethod"

    def "filterOverriddenMethods() WHEN given methods of superclass and subclass from same package SHOULD return correct methods"() {
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

        and:
        Queue<List<Method>> allMethods = new LinkedList<>()
        allMethods.add(superclassMethods)
        allMethods.add(subclassMethods)

        when:
        def filteredMethods = ovverridenMethodsStrategy.filterOverriddenMethods(allMethods)

        then:
        filteredMethods.size() == 2

        and:
        def superclassFilteredMethods = filteredMethods.poll()
        superclassFilteredMethods.size() == 1
        superclassFilteredMethods.contains(superclassMethods[3])

        and:
        def subclassFilteredMethods = filteredMethods.poll()
        subclassFilteredMethods.size() == 4
        subclassFilteredMethods.contains(subclassMethods[0])
        subclassFilteredMethods.contains(subclassMethods[1])
        subclassFilteredMethods.contains(subclassMethods[2])
        subclassFilteredMethods.contains(subclassMethods[3])
    }

    def "filterOverriddenMethods() WHEN given methods of superclass and subclass from another package SHOULD return correct methods"() {
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

        and:
        Queue<List<Method>> allMethods = new LinkedList<>()
        allMethods.add(superclassMethods)
        allMethods.add(subclassMethods)

        when:
        def filteredMethods = ovverridenMethodsStrategy.filterOverriddenMethods(allMethods)

        then:
        filteredMethods.size() == 2

        and:
        def superclassFilteredMethods = filteredMethods.poll()
        superclassFilteredMethods.size() == 2
        superclassFilteredMethods.contains(superclassMethods[1])
        superclassFilteredMethods.contains(superclassMethods[3])

        and:
        def subclassFilteredMethods = filteredMethods.poll()
        subclassFilteredMethods.size() == 4
        subclassFilteredMethods.contains(subclassMethods[0])
        subclassFilteredMethods.contains(subclassMethods[1])
        subclassFilteredMethods.contains(subclassMethods[2])
        subclassFilteredMethods.contains(subclassMethods[3])
    }

    def "filterOverriddenMethods() WHEN given methods of child, parent and grandparent classes SHOULD return correct methods"() {
        given:
        def childMethods = [
                TransitiveOverrideChildClass.getDeclaredMethod("grantParentPublicMethod", String),
                TransitiveOverrideChildClass.getDeclaredMethod("grantParentPackagePrivateMethod", String),
                TransitiveOverrideChildClass.getDeclaredMethod("parentProtectedMethod", String),
                TransitiveOverrideChildClass.getDeclaredMethod("privateMethod", String)
        ]
        def parentMethods = [
                TransitiveOverrideParentClass.getDeclaredMethod("parentPublicMethod", String),
                TransitiveOverrideParentClass.getDeclaredMethod("grantParentPackagePrivateMethod", String),
                TransitiveOverrideParentClass.getDeclaredMethod("parentProtectedMethod", String),
        ]
        def grandParentMethods = [
                TransitiveOverrideGrantParentClass.getDeclaredMethod("grantParentPublicMethod", String),
                TransitiveOverrideGrantParentClass.getDeclaredMethod("grantParentPackagePrivateMethod", String),
                TransitiveOverrideGrantParentClass.getDeclaredMethod("grantParentProtectedMethod", String),
                TransitiveOverrideGrantParentClass.getDeclaredMethod("grantParentPrivateMethod", String)
        ]

        and:
        Queue<List<Method>> allMethods = new LinkedList<>()
        allMethods.add(grandParentMethods)
        allMethods.add(parentMethods)
        allMethods.add(childMethods)

        when:
        def filteredMethods = ovverridenMethodsStrategy.filterOverriddenMethods(allMethods)


        then:
        filteredMethods.size() == 3

        and:
        def grandParentFilteredMethods = filteredMethods.poll()
        grandParentFilteredMethods.size() == 2
        grandParentFilteredMethods.contains(grandParentMethods[2])
        grandParentFilteredMethods.contains(grandParentMethods[3])

        and:
        def parentFilteredMethods = filteredMethods.poll()
        parentFilteredMethods.size() == 1
        parentFilteredMethods.contains(parentMethods[0])

        and:
        def childFilteredMethods = filteredMethods.poll()
        childFilteredMethods.size() == 4
        childFilteredMethods.contains(childMethods[0])
        childFilteredMethods.contains(childMethods[1])
        childFilteredMethods.contains(childMethods[2])
        childFilteredMethods.contains(childMethods[3])
    }
}
