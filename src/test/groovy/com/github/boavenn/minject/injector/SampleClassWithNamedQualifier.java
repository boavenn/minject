package com.github.boavenn.minject.injector;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
@Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
class SampleClassWithNamedQualifier {
    public static final String sampleNamedQualifierValue = "sampleName";

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private Provider<String> sampleProviderField;

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private String sampleField;

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private String sampleMethod(@Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue) String sampleParam) {
        return "";
    }

    @Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue)
    private Provider<String> sampleProviderMethod(@Named(SampleClassWithNamedQualifier.sampleNamedQualifierValue) Provider<String> sampleParam) {
        return () -> "";
    }
}
