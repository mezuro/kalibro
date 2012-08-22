package org.kalibro.core.abstractentity;

import java.util.Collection;
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
		if (value instanceof Collection)
			return collectionEquals((Collection<?>) value, (Collection<?>) otherValue);
		return areDeepEqual(value, otherValue);
	}

	private boolean mapEquals(Map<?, ?> myMap, Map<?, ?> otherMap) {
		if (!areDeepEqual(myMap.keySet(), otherMap.keySet()))
			return false;
		for (Object key : myMap.keySet())
			if (!areDeepEqual(myMap.get(key), otherMap.get(key)))
				return false;
		return true;
	}

	private boolean collectionEquals(Collection<?> myCollection, Collection<?> otherCollection) {
		return areDeepEqual(myCollection.toArray(), otherCollection.toArray());
	}
}