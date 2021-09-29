package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.ConfigurationModule;

class SampleModule implements ConfigurationModule {
    public static final String propertyName = "someProperty";
    public static final String propertyValue = "someValue";
    public static final Integer integerValue = 1024;

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(String.class, propertyName))
              .toInstance(propertyValue);

        binder.bind(Integer.class)
              .toInstance(integerValue);
    }
}
