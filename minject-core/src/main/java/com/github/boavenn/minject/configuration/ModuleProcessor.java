package com.github.boavenn.minject.configuration;

import com.github.boavenn.minject.injector.Injector;

public interface ModuleProcessor {
    void before(Binder binder, Injector injector);
    void process(Module module, Binder binder, Injector injector);
    void after(Binder binder, Injector injector);
}
