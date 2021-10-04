package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.ModuleProcessor;
import com.github.boavenn.minject.injector.Injector;
import lombok.Getter;

@Getter
class SampleModuleProcessor implements ModuleProcessor {
    private int processedModulesCounter = 0;

    @Override
    public void process(Module module, Binder binder, Injector injector) {
        processedModulesCounter++;
    }
}
