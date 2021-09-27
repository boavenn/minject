package com.github.boavenn.minject.instantiation.generic;

import javax.inject.Inject;

abstract class InjectableAbstractMethodsClass {
    @Inject
    private void method1() {

    }

    @Inject
    abstract void method2(String paramA);

    public void method3(String paramA, String paramB) {

    }
}
