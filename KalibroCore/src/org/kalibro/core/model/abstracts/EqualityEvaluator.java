package org.kalibro.core.model.abstracts;

import java.util.List;

class EqualityEvaluator {

	protected EntityReflector reflector;

	protected EqualityEvaluator(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	protected boolean isEquals(Object other) {
		if (other == null)
			return false;
		if (other == reflector.getEntity())
			return true;
		return sameType(other) && sameFieldValues((AbstractEntity<?>) other);
	}

	protected boolean sameType(Object other) {
		return (other instanceof AbstractEntity) && sameIdentityFields((AbstractEntity<?>) other);
	}

	private boolean sameIdentityFields(AbstractEntity<?> other) {
		return new EntityReflector(other).getIdentityFields().equals(reflector.getIdentityFields());
	}

	private boolean sameFieldValues(AbstractEntity<?> other) {
		EntityReflector otherReflector = new EntityReflector(other);
		for (String field : equalityFields()) {
			Object myValue = reflector.get(field);
			Object otherValue = otherReflector.get(field);
			if (!sameFieldValue(myValue, otherValue))
				return false;
		}
		return true;
	}

	protected List<String> equalityFields() {
		return reflector.getIdentityFields();
	}

	protected boolean sameFieldValue(Object myValue, Object otherValue) {
		return myValue.equals(otherValue);
	}
}