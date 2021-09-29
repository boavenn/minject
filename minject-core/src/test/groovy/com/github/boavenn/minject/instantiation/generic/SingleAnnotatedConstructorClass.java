package com.github.boavenn.minject.instantiation.generic;

import javax.inject.Inject;

class SingleAnnotatedConstructorClass {
    @Inject
    public SingleAnnotatedConstructorClass() {

    }

    public SingleAnnotatedConstructorClass(String paramA) {

    }

    public SingleAnnotatedConstructorClass(String paramA, String paramB) {

    }
}
