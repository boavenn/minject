package com.github.boavenn.minject;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Named;

@Getter
class SuperclassClass {
    public static final String fieldName = "superclassField";
    public static final String constructorParamName = "superclassConstructorParam";
    public static final String methodParamName = "superclassMethodParam";

    private final String superclassConstructorParam;

    @Inject
    @Named(fieldName)
    private String superclassField;
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
