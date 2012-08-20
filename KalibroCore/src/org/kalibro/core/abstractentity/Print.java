package org.kalibro.core.abstractentity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows subclasses of {@link AbstractEntity} to specify comments and printing order for the fields at
 * {@code toString()}, making the printing more readable.
 * 
 * @author Carlos Morais
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Print {

	int order() default 0;

	String comment() default "";
}