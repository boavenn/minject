package com.github.boavenn.minject.instantiation.generic;

class CircularDependencyClassA {
    private CircularDependencyClassB classB1;
    private CircularDependencyClassB classB2;

    public CircularDependencyClassA(CircularDependencyClassB classB1, CircularDependencyClassB classB2) {
        this.classB1 = classB1;
        this.classB2 = classB2;
    }
}
