package org.kalibro.core.model.abstracts;

import java.util.*;

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
		if (myValue == null && otherValue == null)
			return true;
		if (myValue == null || otherValue == null)
			return false;
		return deepEquals(myValue, otherValue);
	}

	private boolean deepEquals(Object myValue, Object otherValue) {
		if (myValue instanceof AbstractEntity<?>)
			return ((AbstractEntity<?>) myValue).deepEquals(otherValue);
		if (myValue instanceof Throwable)
			return throwableEquals((Throwable) myValue, (Throwable) otherValue);
		if (myValue instanceof StackTraceElement)
			return stackTraceEquals((StackTraceElement) myValue, (StackTraceElement) otherValue);
		return groupEquals(myValue, otherValue);
	}

	private boolean throwableEquals(Throwable myError, Throwable otherError) {
		return myError.getClass().equals(otherError.getClass())
			&& sameFieldValue(myError.getMessage(), otherError.getMessage())
			&& sameFieldValue(myError.getStackTrace(), otherError.getStackTrace())
			&& sameFieldValue(myError.getCause(), otherError.getCause());
	}

	private boolean stackTraceEquals(StackTraceElement myValue, StackTraceElement otherValue) {
		return sameFieldValue(myValue.getClassName(), otherValue.getClassName())
			&& sameFieldValue(myValue.getMethodName(), otherValue.getMethodName())
			&& sameFieldValue(myValue.getFileName(), otherValue.getFileName())
			&& sameFieldValue(myValue.getLineNumber(), otherValue.getLineNumber());
	}

	private boolean groupEquals(Object myValue, Object otherValue) {
		if (myValue.getClass().isArray())
			return arrayEquals((Object[]) myValue, (Object[]) otherValue);
		if (myValue instanceof Map)
			return mapEquals((Map<?, ?>) myValue, (Map<?, ?>) otherValue);
		if (myValue instanceof Collection)
			return collectionEquals((Collection<?>) myValue, (Collection<?>) otherValue);
		return myValue.equals(otherValue);
	}

	private boolean arrayEquals(Object[] myArray, Object[] otherArray) {
		return collectionEquals(Arrays.asList(myArray), Arrays.asList(otherArray));
	}

	private boolean mapEquals(Map<?, ?> myMap, Map<?, ?> otherMap) {
		if (!collectionEquals(myMap.keySet(), otherMap.keySet()))
			return false;
		for (Object key : myMap.keySet())
			if (!sameFieldValue(myMap.get(key), otherMap.get(key)))
				return false;
		return true;
	}

	private boolean collectionEquals(Collection<?> myCollection, Collection<?> otherCollection) {
		if (myCollection.size() != otherCollection.size())
			return false;
		sortIfPossible(myCollection, otherCollection);
		Iterator<?> otherIterator = otherCollection.iterator();
		for (Object myElement : myCollection)
			if (!sameFieldValue(myElement, otherIterator.next()))
				return false;
		return true;
	}

	@SuppressWarnings("rawtypes" /* impossible to cast without raw types */)
	private void sortIfPossible(Collection<?> myCollection, Collection<?> otherCollection) {
		if (isSortable(myCollection) && isSortable(otherCollection)) {
			Collections.sort((List<Comparable>) myCollection);
			Collections.sort((List<Comparable>) otherCollection);
		}
	}

	private boolean isSortable(Collection<?> collection) {
		return collection instanceof List
			&& !collection.isEmpty()
			&& collection.iterator().next() instanceof Comparable;
	}
}