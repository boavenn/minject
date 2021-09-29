package com.github.boavenn.minject.configuration;

import com.github.boavenn.minject.injector.Injector;

public interface ModuleProcessor {
    void process(Module module, Binder binder, Injector injector);
}
