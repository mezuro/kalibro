package org.kalibro.core.abstractentity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks the field to be used by {@link AbstractEntity} at {@code equals()} and {@code hashCode()}.
 * 
 * @author Carlos Morais
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface IdentityField {
	// No need for value
}