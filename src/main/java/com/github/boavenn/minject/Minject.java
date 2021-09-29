package com.github.boavenn.minject;

import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.ModuleProcessor;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.injector.generic.GenericInjectorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Minject {
    public static Injector createInjector() {
        return createInjectorWith(Collections.emptyList());
    }

    public static Injector createInjectorWith(Module... modules) {
        return createInjectorWith(Arrays.asList(modules));
    }

    public static Injector createInjectorWith(List<Module> modules) {
        return createInjectorWith(modules, Collections.emptyList());
    }

    public static Injector createInjectorWith(List<Module> modules, List<ModuleProcessor> moduleProcessors) {
        return new GenericInjectorFactory().addModules(modules)
                                           .addModuleProcessors(moduleProcessors)
                                           .create();
    }
}
