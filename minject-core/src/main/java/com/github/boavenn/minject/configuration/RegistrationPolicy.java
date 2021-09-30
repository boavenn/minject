package com.github.boavenn.minject.configuration;

import com.github.boavenn.minject.configuration.registration.RegistrationReplacingStrategy;
import com.github.boavenn.minject.configuration.registration.RegistrationThrowingStrategy;
import lombok.Getter;

public enum RegistrationPolicy {
    REPLACE(RegistrationReplacingStrategy.create()),
    THROW(RegistrationThrowingStrategy.create());

    @Getter
    private final RegistrationStrategy strategy;

    RegistrationPolicy(RegistrationStrategy strategy) {
        this.strategy = strategy;
    }
}
