package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.ConfigurationModule;

class SampleModuleB implements ConfigurationModule {
    public static final String propertyName = "somePropertyFromModuleB";
    public static final String propertyValue = "someValueInModuleB";

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(String.class, propertyName))
              .toInstance(propertyValue);
    }
}
