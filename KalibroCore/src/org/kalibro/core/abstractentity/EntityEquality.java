package org.kalibro.core.abstractentity;

import java.util.List;

/**
 * Determines equality of entities based on fields annotated with {@link IdentityField}.
 * 
 * @author Carlos Morais
 */
class EntityEquality extends Equality<AbstractEntity<?>> {

	private boolean deep;
	protected EntityReflector reflector, otherReflector;

	public EntityEquality(boolean deep) {
		this.deep = deep;
	}

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

	private boolean sameType() {
		if (deep)
			return reflector.getObjectClass() == otherReflector.getObjectClass();
		return reflector.listIdentityFields().equals(otherReflector.listIdentityFields());
	}

	private boolean sameFieldValues() {
		for (String field : equalityFields())
			if (!sameFieldValue(field))
				return false;
		return true;
	}

	private List<String> equalityFields() {
		if (deep)
			return reflector.listFields();
		return reflector.listIdentityFields();
	}

	private boolean sameFieldValue(String field) {
		return areEqual(reflector.get(field), otherReflector.get(field), deep);
	}
}