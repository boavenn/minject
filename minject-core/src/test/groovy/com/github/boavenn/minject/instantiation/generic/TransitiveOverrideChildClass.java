package com.github.boavenn.minject.instantiation.generic;

class TransitiveOverrideChildClass extends TransitiveOverrideParentClass {
    public String grantParentPublicMethod(String param) {
        return "";
    }

    String grantParentPackagePrivateMethod(String param) {
        return "";
    }

    protected String parentProtectedMethod(String param) {
        return "";
    }

    private String privateMethod(String param) {
        return "";
    }
}
