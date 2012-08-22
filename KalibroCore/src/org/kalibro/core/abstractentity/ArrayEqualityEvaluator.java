package org.kalibro.core.abstractentity;

/**
 * Determines equality of arrays deeply for each element.
 * 
 * @author Carlos Morais
 */
class ArrayEqualityEvaluator extends EqualityEvaluator<Object[]> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value.getClass().isArray();
	}

	@Override
	protected boolean equals(Object[] array, Object[] other) {
		if (array.length != other.length)
			return false;
		for (int i = 0; i < array.length; i++)
			if (!areDeepEqual(array[i], other[i]))
				return false;
		return true;
	}
}