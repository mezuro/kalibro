package org.kalibro.core.abstractentity;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies the methods to be used by {@link AbstractEntity} at {@code compareTo()}.
 * 
 * @author Carlos Morais
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SortingMethods {

	String[] value();
}