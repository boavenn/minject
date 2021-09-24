package com.github.boavenn.minject;

import javax.inject.Singleton;

@Singleton
@CustomQualifier(key1 = "value1", key2 = "value2")
class SampleClassWithCustomQualifier {
    @CustomQualifier(key1 = "value1", key2 = "value2")
    private String sampleField;

    @CustomQualifier(key1 = "value1", key2 = "value2")
    private String sampleMethod(@CustomQualifier(key1 = "value1", key2 = "value2") String sampleParam) {
        return "";
    }
}
