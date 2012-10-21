package org.kalibro.core.abstractentity;

/**
 * Determines equality of arrays. Two arrays are equal when they contain the same elements in the same order.
 * 
 * @author Carlos Morais
 */
class ArrayEquality extends Equality<Object[]> {

	private boolean deep;

	public ArrayEquality(boolean deep) {
		this.deep = deep;
	}

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Object[];
	}

	@Override
	protected boolean equals(Object[] array, Object[] other) {
		if (array.length != other.length)
			return false;
		for (int i = 0; i < array.length; i++)
			if (!areEqual(array[i], other[i], deep))
				return false;
		return true;
	}
}