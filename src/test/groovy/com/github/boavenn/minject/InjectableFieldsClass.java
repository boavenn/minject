package com.github.boavenn.minject;

import javax.inject.Inject;

class InjectableFieldsClass {
    @Inject
    private String field1;

    @Inject
    String field2;

    public String field3;
}
