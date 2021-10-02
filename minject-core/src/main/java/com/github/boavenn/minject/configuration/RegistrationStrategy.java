package com.github.boavenn.minject.configuration;

import java.util.function.Supplier;

public interface RegistrationStrategy {
    <T> T register(Supplier<T> registrationCallback, boolean exists, String key);
}
