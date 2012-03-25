package org.kalibro.core.model.abstracts;

class HashCodeCalculator {

	private EntityReflector reflector;

	protected HashCodeCalculator(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	protected int calculate() {
		Integer codeSum = 1;
		for (String field : reflector.listIdentityFields())
			codeSum += reflector.get(field).hashCode();
		return codeSum.hashCode();
	}
}