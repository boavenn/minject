package com.github.boavenn.minject;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@interface CustomQualifier {
    String key1() default "";
    String key2() default "";
}
