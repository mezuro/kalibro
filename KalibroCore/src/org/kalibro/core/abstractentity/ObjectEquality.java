package org.kalibro.core.abstractentity;

/**
 * Default equality. Determines equality of objects using {@code Object.equals()}.
 * 
 * @author Carlos Morais
 */
class ObjectEquality extends Equality<Object> {

	@Override
	protected boolean canEvaluate(Object value) {
		return true;
	}

	@Override
	protected boolean equals(Object value, Object other) {
		return value.equals(other);
	}
}