package org.kalibro.core.abstractentity;

import java.util.List;

/**
 * Determines equality of entities using identity fields.
 * 
 * @author Carlos Morais
 */
class EqualityEvaluator {

	protected EntityReflector reflector, otherReflector;

	protected EqualityEvaluator(AbstractEntity<?> entity, Object other) {
		reflector = new EntityReflector(entity);
		if (other instanceof AbstractEntity)
			otherReflector = new EntityReflector((AbstractEntity<?>) other);
	}

	protected boolean areEqual() {
		if (otherReflector == null)
			return false;
		if (reflector.getObject() == otherReflector.getObject())
			return true;
		return sameType() && sameFieldValues();
	}

	protected boolean sameType() {
		return reflector.listIdentityFields().equals(otherReflector.listIdentityFields());
	}

	private boolean sameFieldValues() {
		for (String field : equalityFields())
			if (!sameFieldValues(field))
				return false;
		return true;
	}

	protected List<String> equalityFields() {
		return reflector.listIdentityFields();
	}

	private boolean sameFieldValues(String field) {
		return areEqual(reflector.get(field), otherReflector.get(field));
	}

	protected boolean areEqual(Object myValue, Object otherValue) {
		if (myValue == null)
			return otherValue == null;
		return sameValue(myValue, otherValue);
	}

	protected boolean sameValue(Object myValue, Object otherValue) {
		return myValue.equals(otherValue);
	}
}