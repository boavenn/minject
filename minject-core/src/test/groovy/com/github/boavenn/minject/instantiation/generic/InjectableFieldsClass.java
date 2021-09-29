package com.github.boavenn.minject.instantiation.generic;

import javax.inject.Inject;

class InjectableFieldsClass {
    @Inject
    private String field1;

    @Inject
    String field2;

    public String field3;
}
