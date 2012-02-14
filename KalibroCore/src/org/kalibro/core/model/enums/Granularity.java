package org.kalibro.core.model.enums;

import org.kalibro.core.util.Identifier;

/**
 * Each granularity represents the type of a {@link Module} which contains modules of the same or next type.
 * 
 * @author Carlos Morais
 */
public enum Granularity {

	APPLICATION, PACKAGE, CLASS, METHOD;

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public Granularity inferParentGranularity() {
		if (ordinal() > 1)
			return values()[ordinal() - 1];
		return this;
	}
}