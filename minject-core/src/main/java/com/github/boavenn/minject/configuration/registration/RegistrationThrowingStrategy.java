package com.github.boavenn.minject.configuration.registration;

import com.github.boavenn.minject.configuration.RegistrationStrategy;
import com.github.boavenn.minject.exceptions.BindingException;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(staticName = "create")
public class RegistrationThrowingStrategy implements RegistrationStrategy {
    @Override
    public <T> T register(Supplier<T> registrationCallback, boolean exists, String key) {
        if (exists) {
            throw BindingException.bindingAlreadyExists(key);
        }
        return registrationCallback.get();
    }
}
