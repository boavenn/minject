package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.ConfigurationModule;
import com.github.boavenn.minject.configuration.Provides;
import com.github.boavenn.minject.injector.Injector;

import javax.inject.Named;

class SampleModuleB implements ConfigurationModule {
    public static final String propertyName = "somePropertyFromModuleB";
    public static final String propertyValue = "someValueInModuleB";

    public static final String providedStringName = "moduleBProvides";

    @Override
    public void configure(Binder binder) {
        binder.bind(ClassKey.of(String.class, propertyName))
              .toInstance(propertyValue);
    }

    @Provides
    @Named(providedStringName)
    public String publicProvides(@Named(propertyName) String b,
                                 Injector injector) {
        return b + injector.getProviderOf(Integer.class).get();
    }
}
