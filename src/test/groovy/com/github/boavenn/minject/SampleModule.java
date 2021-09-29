package com.github.boavenn.minject;

import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

class SampleModule implements Module {
    public static final String propertyName = "someProperty";
    public static final String propertyValue = "someValue";
    public static final Integer integerValue = 1024;

    public static final String providedStringName = "provides";
    public static final String providedStringValue = "providesValue";

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(String.class, propertyName))
              .toInstance(propertyValue);

        binder.bind(Integer.class)
              .toInstance(integerValue);
    }

    @Provides
    @Singleton
    @Named(providedStringName)
    public String publicProvides() {
        return providedStringValue;
    }
}
