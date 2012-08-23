package org.kalibro.core.abstractentity;

import java.util.List;

/**
 * Determines equality of entities using all fields, and recursively on composed entities.
 * 
 * @author Carlos Morais
 */
class DeepEntityEquality extends EntityEquality {

	@Override
	protected boolean sameType() {
		return reflector.getObjectClass() == otherReflector.getObjectClass();
	}

	@Override
	protected List<String> equalityFields() {
		return reflector.listFields();
	}

	@Override
	protected boolean sameValue(Object value, Object otherValue) {
		return areDeepEqual(value, otherValue);
	}
}