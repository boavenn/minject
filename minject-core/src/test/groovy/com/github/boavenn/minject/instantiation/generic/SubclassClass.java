package com.github.boavenn.minject.instantiation.generic;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Getter
class SubclassClass extends SuperclassClass {
    public static final String fieldName = "subclassField";
    public static final String constructorParamName = "subclassConstructorParam";
    public static final String methodParamName = "subclassMethodParam";

    private final String subclassConstructorParam;

    @Inject
    @Named(fieldName)
    private String subclassField;

    @Inject
    @Named(fieldName)
    private Provider<String> subclassFieldProvider;

    private String subclassMethodParam = "";

    @Inject
    public SubclassClass(@Named(SuperclassClass.constructorParamName) String superclassConstructorParam,
                         @Named(constructorParamName) String subclassConstructorParam) {
        super(superclassConstructorParam);
        this.subclassConstructorParam = subclassConstructorParam;
    }

    @Inject
    public void setSubclassMethodParam(@Named(methodParamName) String subclassMethodParam) {
        this.subclassMethodParam = subclassMethodParam;
    }
}
