package org.kalibro.core.model.abstracts;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

class DeepEqualityEvaluator extends EqualityEvaluator {

	protected DeepEqualityEvaluator(AbstractEntity<?> entity) {
		super(entity);
	}

	@Override
	protected boolean sameType(Object other) {
		return reflector.getEntityClass() == other.getClass();
	}

	@Override
	protected List<String> equalityFields() {
		return reflector.listAllFields();
	}

	@Override
	protected boolean sameFieldValue(Object myValue, Object otherValue) {
		if (myValue == null)
			return otherValue == null;
		return deepEquals(myValue, otherValue);
	}

	private boolean deepEquals(Object myValue, Object otherValue) {
		if (myValue instanceof AbstractEntity<?>)
			return ((AbstractEntity<?>) myValue).deepEquals(otherValue);
		return specialEquals(myValue, otherValue);
	}

	private boolean specialEquals(Object myValue, Object otherValue) {
		if (myValue instanceof Collection<?>)
			return collectionEquals((Collection<?>) myValue, (Collection<?>) otherValue);
		if (myValue instanceof Map<?, ?>)
			return mapEquals((Map<?, ?>) myValue, (Map<?, ?>) otherValue);
		if (myValue.getClass().isArray())
			return collectionEquals(Arrays.asList((Object[]) myValue), Arrays.asList((Object[]) otherValue));
		return myValue.equals(otherValue);
	}

	private boolean collectionEquals(Collection<?> myCollection, Collection<?> otherCollection) {
		if (myCollection.size() != otherCollection.size())
			return false;
		for (Object myElement : myCollection)
			if (!sameFieldValue(myElement, find(myElement, otherCollection)))
				return false;
		return true;
	}

	private Object find(Object element, Collection<?> collection) {
		for (Object anElement : collection)
			if (element.equals(anElement))
				return anElement;
		return null;
	}

	private boolean mapEquals(Map<?, ?> myMap, Map<?, ?> otherMap) {
		if (!collectionEquals(myMap.keySet(), otherMap.keySet()))
			return false;
		for (Object key : myMap.keySet())
			if (!sameFieldValue(myMap.get(key), otherMap.get(key)))
				return false;
		return true;
	}
}