package com.github.boavenn.minject.instantiation.generic;

class CircularDependencyClassC {
    private CircularDependencyClassA classA;

    public CircularDependencyClassC(CircularDependencyClassA classA) {
        this.classA = classA;
    }
}
