package org.kalibro.core.model.abstracts;

public class HashCodeCalculator {

	private EntityReflector reflector;

	public HashCodeCalculator(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	public int calculate() {
		Integer codeSum = 1;
		for (String field : reflector.getIdentityFields())
			codeSum += reflector.get(field).hashCode();
		return codeSum.hashCode();
	}
}