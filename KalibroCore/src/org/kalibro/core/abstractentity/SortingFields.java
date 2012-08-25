package org.kalibro.core.abstractentity;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies the fields to be used in comparisons.<br/>
 * The comparisons will occur in the order the fields are listed. Any given field will be used in comparison only if all
 * previous fields comparisons resulted in 0.<br/>
 * All listed fields should be comparable, and subclasses should not override the parent's sorting annotation.
 * 
 * @see AbstractEntity
 * @author Carlos Morais
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SortingFields {

	String[] value();
}