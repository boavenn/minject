package com.github.boavenn.minject.instantiation.generic;

class TransitiveOverrideGrantParentClass {
    public String grantParentPublicMethod(String param) {
        return "";
    }

    String grantParentPackagePrivateMethod(String param) {
        return "";
    }

    protected String grantParentProtectedMethod(String param) {
        return "";
    }

    private String grantParentPrivateMethod(String param) {
        return "";
    }
}
