package org.kalibro.core.abstractentity;

/**
 * Determines equality of arrays deeply for each element. Two arrays are equal when they contain the same elements in
 * the same order.
 * 
 * @author Carlos Morais
 */
class ArrayEquality extends Equality<Object[]> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Object[];
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