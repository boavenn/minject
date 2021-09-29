package com.github.boavenn.minject.configuration.generic;

import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.ConfigurationModule;
import com.github.boavenn.minject.configuration.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

class SampleModule implements ConfigurationModule {
    @Provides
    @Override
    public void configure(Binder binder) {

    }

    @Provides
    @Named("public")
    @Singleton
    public String publicProvides() {
        return "public";
    }

    @Provides
    @Named("package")
    @Singleton
    String packageProvides() {
        return "package";
    }

    @Provides
    @Named("protected")
    protected String protectedProvides() {
        return "protected";
    }

    @Provides
    @Named("private")
    private String privateProvides() {
        return "private";
    }
}
