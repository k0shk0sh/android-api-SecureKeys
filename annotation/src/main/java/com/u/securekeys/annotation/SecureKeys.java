package com.u.securekeys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for saving as secure a set of key-value. Add it were you like most.
 * Created by saguilera on 3/3/17.
 */
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface SecureKeys {

    String CLASSPATH = "com.u.securekeys.annotation.SecureKeys";

    SecureKey[] value();

}