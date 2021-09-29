package com.github.boavenn.minject;

import javax.inject.Provider;

class SampleClassWithNoQualifier {
    private Provider<String> sampleProviderField;
    private String sampleField;

    private String sampleMethod(String sampleParam) {
        return "";
    }

    private Provider<String> sampleProviderMethod(Provider<String> sampleParam) {
        return () -> "";
    }

    private void sampleVoidMethod() {

    }
}
