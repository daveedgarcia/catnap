package com.catnap.springmvc.annotation;

import com.catnap.core.annotation.CatnapAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Woody on 1/1/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@CatnapAnnotation
public @interface CatnapResponseBody
{
    /**
     * Optional argument that defines the name of the desired
     * object in the returned model you wish to have rendered
     * by Catnap.  If a value is not supplied the class name
     * of the return type of the annotated method will be
     * used.
     */
    public String value() default "";
}
