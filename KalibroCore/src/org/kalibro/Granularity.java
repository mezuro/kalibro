package org.kalibro;

import org.kalibro.core.Identifier;

/**
 * Granularity of the measured {@link Module}.
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