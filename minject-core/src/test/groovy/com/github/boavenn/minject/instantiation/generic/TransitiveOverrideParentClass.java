package com.github.boavenn.minject.instantiation.generic;

class TransitiveOverrideParentClass extends TransitiveOverrideGrantParentClass {
    public String parentPublicMethod(String param) {
        return "";
    }

    String grantParentPackagePrivateMethod(String param) {
        return "";
    }

    protected String parentProtectedMethod(String param) {
        return "";
    }
}
