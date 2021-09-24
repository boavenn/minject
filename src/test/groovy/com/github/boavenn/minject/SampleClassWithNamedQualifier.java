package com.github.boavenn.minject;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
class SampleClassWithNamedQualifier {
    public static final String sampleNamedQualifierValue = "sampleName";

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private String sampleField;

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private String sampleMethod(@Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue) String sampleParam) {
        return "";
    }
}
