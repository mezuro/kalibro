package org.kalibro.core.abstractentity;

import java.util.List;

/**
 * Determines equality of entities using identity fields.
 * 
 * @author Carlos Morais
 */
class EntityEquality extends Equality<AbstractEntity<?>> {

	protected EntityReflector reflector, otherReflector;

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof AbstractEntity;
	}

	@Override
	protected boolean equals(AbstractEntity<?> entity, AbstractEntity<?> other) {
		reflector = new EntityReflector(entity);
		otherReflector = new EntityReflector(other);
		return sameType() && sameFieldValues();
	}

	protected boolean sameType() {
		return reflector.listIdentityFields().equals(otherReflector.listIdentityFields());
	}

	private boolean sameFieldValues() {
		for (String field : equalityFields())
			if (!sameFieldValue(field))
				return false;
		return true;
	}

	protected List<String> equalityFields() {
		return reflector.listIdentityFields();
	}

	private boolean sameFieldValue(String field) {
		return sameValue(reflector.get(field), otherReflector.get(field));
	}

	protected boolean sameValue(Object value, Object otherValue) {
		return areEqual(value, otherValue);
	}
}