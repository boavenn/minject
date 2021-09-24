package com.github.boavenn.minject;

import java.lang.reflect.Field;
import java.util.List;

public interface InjectableFieldsResolver {
    <T> List<Field> findInjectableFieldsIn(Class<T> cls);
}
