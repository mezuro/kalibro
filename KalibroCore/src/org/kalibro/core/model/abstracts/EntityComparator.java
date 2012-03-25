package org.kalibro.core.model.abstracts;

import java.lang.reflect.Method;

class EntityComparator<T extends Comparable<? super T>> {

	private EntityReflector reflector;

	protected EntityComparator(AbstractEntity<T> entity) {
		reflector = new EntityReflector(entity);
	}

	protected int compare(T other) {
		for (Method method : reflector.listSortingMethods()) {
			int compare = compare(other, method);
			if (compare != 0)
				return compare;
		}
		return 0;
	}

	private int compare(T other, Method method) {
		try {
			return doCompare(other, method);
		} catch (Exception exception) {
			return 0;
		}
	}

	private int doCompare(T other, Method method) throws Exception {
		Object myValue = reflector.invoke(method);
		Object otherValue = new EntityReflector((AbstractEntity<?>) other).invoke(method);
		Method compareTo = myValue.getClass().getMethod("compareTo", Object.class);
		return (Integer) compareTo.invoke(myValue, otherValue);
	}
}