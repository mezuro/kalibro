package org.kalibro.core.abstractentity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the field to be ignored in reflective operations by {@link AbstractEntity}.
 * 
 * @author Carlos Morais
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Ignore {
	// No need for value
}