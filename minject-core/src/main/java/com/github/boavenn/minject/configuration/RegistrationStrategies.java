package com.github.boavenn.minject.configuration;

import com.github.boavenn.minject.configuration.registration.RegistrationReplacingStrategy;
import com.github.boavenn.minject.configuration.registration.RegistrationThrowingStrategy;

import java.util.function.Supplier;

public enum RegistrationStrategies implements RegistrationStrategy {
    REPLACE(RegistrationReplacingStrategy.create()),
    THROW(RegistrationThrowingStrategy.create());

    private final RegistrationStrategy strategy;

    RegistrationStrategies(RegistrationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public <T> T register(Supplier<T> registrationCallback, boolean exists, String key) {
        return strategy.register(registrationCallback, exists, key);
    }
}
