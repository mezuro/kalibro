package org.kalibro.core.abstractentity;

/**
 * Calculates hash codes for entities, using fields annotated with {@link IdentityField}.<br/>
 * Based on recommendations presented by Joshua Bloch at <i>Effective Java</i>, Item 8.
 * 
 * @author Carlos Morais
 */
final class HashCodeCalculator {

	private static final int SEED = 23;
	private static final int PRIME = 37;

	protected static int hash(AbstractEntity<?> entity) {
		EntityReflector reflector = new EntityReflector(entity);
		int result = SEED;
		for (String field : reflector.listIdentityFields())
			result = hash(result, reflector.get(field));
		return result;
	}

	private static int hash(int seed, Object object) {
		if (object == null)
			return 0;
		if (!object.getClass().isArray())
			return (PRIME * seed) + object.hashCode();
		int result = SEED;
		for (Object element : (Object[]) object)
			result = hash(result, element);
		return result;
	}

	private HashCodeCalculator() {
		return;
	}
}