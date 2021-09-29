package com.github.boavenn.minject.instantiation.generic;

class SubclassOverrideClass extends SuperclassOverrideClass {
    public String publicMethod(String param) {
        return "";
    }

    String packagePrivateMethod(String param) {
        return "";
    }

    protected String protectedMethod(String param) {
        return "";
    }

    private String privateMethod(String param) {
        return "";
    }
}
