package com.github.boavenn.minject.instantiation.generic;

import javax.inject.Inject;

class MultipleAnnotatedConstructorsClass {
    @Inject
    public MultipleAnnotatedConstructorsClass() {

    }

    @Inject
    public MultipleAnnotatedConstructorsClass(String paramA) {

    }

    @Inject
    public MultipleAnnotatedConstructorsClass(String paramA, String paramB) {

    }
}
