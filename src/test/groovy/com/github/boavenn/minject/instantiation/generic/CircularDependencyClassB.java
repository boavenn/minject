package com.github.boavenn.minject.instantiation.generic;

class CircularDependencyClassB {
    private CircularDependencyClassC classC;

    public CircularDependencyClassB(CircularDependencyClassC classC) {
        this.classC = classC;
    }
}
