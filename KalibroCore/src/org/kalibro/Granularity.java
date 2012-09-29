package org.kalibro;

import org.kalibro.core.Identifier;

/**
 * Each granularity represents the type of a {@link Module} which contains modules of the same or next type.
 * 
 * @author Carlos Morais
 */
public enum Granularity {

	SOFTWARE, PACKAGE, CLASS, METHOD;

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public Granularity inferParentGranularity() {
		if (ordinal() <= 1)
			return this;
		return values()[ordinal() - 1];
	}
}