package org.kalibro.core.abstractentity;

import java.util.Iterator;
import java.util.List;

/**
 * Determines equality of lists. Two lists are equal when they contain the same elements in the same order.
 * 
 * @author Carlos Morais
 */
class ListEquality extends Equality<List<?>> {

	private boolean deep;

	public ListEquality(boolean deep) {
		this.deep = deep;
	}

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof List;
	}

	@Override
	protected boolean equals(List<?> list, List<?> other) {
		if (list.size() != other.size())
			return false;
		Iterator<?> iterator = list.iterator();
		Iterator<?> otherIterator = other.iterator();
		while (iterator.hasNext())
			if (!areEqual(iterator.next(), otherIterator.next(), deep))
				return false;
		return true;
	}
}