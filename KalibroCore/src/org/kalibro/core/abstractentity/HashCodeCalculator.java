package org.kalibro.core.abstractentity;

/**
 * Calculates hash codes for entities, based on identity fields.<br/>
 * Based on the recommendations of Effective Java, by Joshua Bloch.
 * 
 * @author Carlos Morais
 */
class HashCodeCalculator {

	private static final int SEED = 23;
	private static final int PRIME = 37;

	private EntityReflector reflector;

	protected HashCodeCalculator(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	protected int calculate() {
		int result = SEED;
		for (String field : reflector.listIdentityFields())
			result = hash(result, reflector.get(field));
		return result;
	}

	private int hash(int seed, Object object) {
		return (PRIME * seed) + (object == null ? 0 : object.hashCode());
	}
}