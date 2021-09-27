package com.github.boavenn.minject.binding.generic;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Getter
class SuperclassClass {
    public static final String fieldName = "superclassField";
    public static final String constructorParamName = "superclassConstructorParam";
    public static final String methodParamName = "superclassMethodParam";

    private final String superclassConstructorParam;

    @Inject
    @Named(fieldName)
    private String superclassField;

    @Inject
    @Named(fieldName)
    private Provider<String> superclassFieldProvider;

    private String superclassMethodParam = "";

    @Inject
    public SuperclassClass(@Named(constructorParamName) String superclassConstructorParam) {
        this.superclassConstructorParam = superclassConstructorParam;
    }

    @Inject
    public void setSuperclassMethodParam(@Named(methodParamName) String superclassMethodParam) {
        this.superclassMethodParam = superclassMethodParam;
    }
}
