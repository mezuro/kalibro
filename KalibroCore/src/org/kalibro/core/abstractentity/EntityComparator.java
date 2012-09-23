package org.kalibro.core.abstractentity;

import java.lang.reflect.Method;

import org.kalibro.KalibroError;
import org.kalibro.core.reflection.FieldReflector;

/**
 * Compares entities based on fields specified at {@link SortingFields} annotation.
 * 
 * @author Carlos Morais
 */
class EntityComparator<T extends Comparable<? super T>> {

	private int comparison;
	private FieldReflector otherReflector;
	private EntityReflector reflector;

	protected int compare(AbstractEntity<T> entity, T other) {
		comparison = 0;
		reflector = new EntityReflector(entity);
		otherReflector = new FieldReflector(other);
		for (String field : reflector.listSortingFields())
			if (compare(field))
				return comparison;
		return comparison;
	}

	private boolean compare(String field) {
		try {
			comparison = doCompare(reflector.get(field), otherReflector.get(field));
			return comparison != 0;
		} catch (Exception exception) {
			String fieldName = reflector.getObjectClass().getName() + "." + field;
			throw new KalibroError("Error comparing fields: " + fieldName, exception);
		}
	}

	private int doCompare(Object value, Object other) throws Exception {
		Method compareTo = value.getClass().getMethod("compareTo", Object.class);
		return (Integer) compareTo.invoke(value, other);
	}
}