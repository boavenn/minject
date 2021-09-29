package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.Provides;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

class SampleModuleA implements Module {
    public static final String propertyName = "someProperty";
    public static final String propertyValue = "someValue";
    public static final Integer integerValue = 1024;

    public static final String providedStringName = "moduleAProvides";

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(String.class, propertyName))
              .toInstance(propertyValue);

        binder.bind(Integer.class)
              .toInstance(integerValue);

        binder.install(new SampleModuleB());
    }

    @Provides
    @Singleton
    @Named(providedStringName)
    public String publicProvides(@Named(propertyName) String a,
                                 @Named(SampleModuleB.propertyName) Provider<String> b) {
        return a + b.get();
    }
}
