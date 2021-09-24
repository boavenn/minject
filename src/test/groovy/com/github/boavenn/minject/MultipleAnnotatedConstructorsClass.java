package com.github.boavenn.minject;

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
