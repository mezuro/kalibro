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
		if (value == null)
			return otherValue == null;
		if (value == otherValue)
			return true;
		if (value instanceof AbstractEntity<?>)
			return ((AbstractEntity<?>) value).deepEquals(otherValue);
		if (value instanceof Throwable)
			return throwableEquals((Throwable) value, (Throwable) otherValue);
		if (value instanceof Map)
			return mapEquals((Map<?, ?>) value, (Map<?, ?>) otherValue);
		if (value instanceof Collection)
			return collectionEquals((Collection<?>) value, (Collection<?>) otherValue);
		if (value.getClass().isArray())
			return arrayEquals((Object[]) value, (Object[]) otherValue);
		return areEqual(value, otherValue);
	}

	private boolean throwableEquals(Throwable myError, Throwable otherError) {
		return myError.getClass() == otherError.getClass()
			&& sameValue(myError.getMessage(), otherError.getMessage())
			&& sameValue(myError.getStackTrace(), otherError.getStackTrace())
			&& sameValue(myError.getCause(), otherError.getCause());
	}

	private boolean mapEquals(Map<?, ?> myMap, Map<?, ?> otherMap) {
		if (!collectionEquals(myMap.keySet(), otherMap.keySet()))
			return false;
		for (Object key : myMap.keySet())
			if (!sameValue(myMap.get(key), otherMap.get(key)))
				return false;
		return true;
	}

	private boolean collectionEquals(Collection<?> myCollection, Collection<?> otherCollection) {
		return arrayEquals(myCollection.toArray(), otherCollection.toArray());
	}

	private boolean arrayEquals(Object[] myArray, Object[] otherArray) {
		if (myArray.length != otherArray.length)
			return false;
		for (int i = 0; i < myArray.length; i++)
			if (!sameValue(myArray[i], otherArray[i]))
				return false;
		return true;
	}
}