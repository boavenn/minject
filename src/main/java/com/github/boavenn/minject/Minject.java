package com.github.boavenn.minject;

import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.injector.generic.GenericInjectorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Minject {
    public static Injector createInjector() {
        var genericInjectorFactory = new GenericInjectorFactory();
        return genericInjectorFactory.create();
    }
}
