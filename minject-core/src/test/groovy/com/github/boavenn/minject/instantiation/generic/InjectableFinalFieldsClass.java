package com.github.boavenn.minject.instantiation.generic;

import javax.inject.Inject;

class InjectableFinalFieldsClass {
    @Inject
    private String field1;

    @Inject
    final String field2 = "";

    public String field3;
}
