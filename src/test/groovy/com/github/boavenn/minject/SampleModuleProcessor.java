package com.github.boavenn.minject;

import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.ModuleProcessor;
import com.github.boavenn.minject.injector.Injector;
import lombok.Getter;

@Getter
public class SampleModuleProcessor implements ModuleProcessor {
    private int processedModulesCounter = 0;

    @Override
    public void process(Module module, Binder binder, Injector injector) {
        processedModulesCounter++;
    }
}
