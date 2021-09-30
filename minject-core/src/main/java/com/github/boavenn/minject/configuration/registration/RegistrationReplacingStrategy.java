package com.github.boavenn.minject.configuration.registration;

import com.github.boavenn.minject.configuration.RegistrationStrategy;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(staticName = "create")
public class RegistrationReplacingStrategy implements RegistrationStrategy {
    @Override
    public <T> T register(Supplier<T> registrationCallback, boolean alreadyExists, String objRepresentation) {
        return registrationCallback.get();
    }
}
