package org.kalibro.core.abstractentity;

import java.util.Iterator;
import java.util.List;

/**
 * Determines equality of lists deeply for each element.
 * 
 * @author Carlos Morais
 */
class ListEqualityEvaluator extends EqualityEvaluator<List<?>> {

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
			if (!areDeepEqual(iterator.next(), otherIterator.next()))
				return false;
		return true;
	}
}