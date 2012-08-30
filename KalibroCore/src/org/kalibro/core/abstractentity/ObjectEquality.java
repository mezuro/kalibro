package org.kalibro.core.abstractentity;

/**
 * Determines equality of objects.
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