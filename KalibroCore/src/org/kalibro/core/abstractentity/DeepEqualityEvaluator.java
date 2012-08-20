package org.kalibro.core.abstractentity;

import java.util.*;

class DeepEqualityEvaluator extends EqualityEvaluator {

	protected DeepEqualityEvaluator(AbstractEntity<?> entity, Object other) {
		super(entity, other);
	}

	@Override
	protected boolean sameType() {
		return reflector.getObjectClass() == otherReflector.getObjectClass();
	}

	@Override
	protected List<String> equalityFields() {
		return reflector.listFields();
	}

	@Override
	protected boolean sameValue(Object myValue, Object otherValue) {
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
			&& areEqual(myError.getMessage(), otherError.getMessage())
			&& areEqual(myError.getStackTrace(), otherError.getStackTrace())
			&& areEqual(myError.getCause(), otherError.getCause());
	}

	private boolean stackTraceEquals(StackTraceElement myValue, StackTraceElement otherValue) {
		return areEqual(myValue.getClassName(), otherValue.getClassName())
			&& areEqual(myValue.getMethodName(), otherValue.getMethodName())
			&& areEqual(myValue.getFileName(), otherValue.getFileName())
			&& areEqual(myValue.getLineNumber(), otherValue.getLineNumber());
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
			if (!areEqual(myMap.get(key), otherMap.get(key)))
				return false;
		return true;
	}

	private boolean collectionEquals(Collection<?> myCollection, Collection<?> otherCollection) {
		if (myCollection.size() != otherCollection.size())
			return false;
		Iterator<?> otherIterator = otherCollection.iterator();
		for (Object myElement : myCollection)
			if (!areEqual(myElement, otherIterator.next()))
				return false;
		return true;
	}
}