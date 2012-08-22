package org.kalibro.core.abstractentity;

import java.util.List;
import java.util.Map;

/**
 * Determines equality of entities using all fields, and recursively on composed entities.
 * 
 * @author Carlos Morais
 */
class DeepEntityEqualityEvaluator extends EntityEqualityEvaluator {

	@Override
	protected boolean sameType() {
		return reflector.getObjectClass() == otherReflector.getObjectClass();
	}

	@Override
	protected List<String> equalityFields() {
		return reflector.listFields();
	}

	@Override
	protected boolean sameValue(Object value, Object otherValue) {
		if (value instanceof Map)
			return mapEquals((Map<?, ?>) value, (Map<?, ?>) otherValue);
		return areDeepEqual(value, otherValue);
	}

	private boolean mapEquals(Map<?, ?> map, Map<?, ?> other) {
		if (!areDeepEqual(map.keySet(), other.keySet()))
			return false;
		for (Object key : map.keySet())
			if (!areDeepEqual(map.get(key), other.get(key)))
				return false;
		return true;
	}
}