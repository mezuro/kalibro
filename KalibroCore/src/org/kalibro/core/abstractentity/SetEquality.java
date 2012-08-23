package org.kalibro.core.abstractentity;

import java.util.Set;

/**
 * Determines equality of sets deeply for each element.
 * 
 * @author Carlos Morais
 */
class SetEquality extends Equality<Set<?>> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Set;
	}

	@Override
	protected boolean equals(Set<?> set, Set<?> other) {
		if (set.size() != other.size())
			return false;
		for (Object element : set)
			if (!contains(other, element))
				return false;
		return true;
	}

	private boolean contains(Set<?> set, Object object) {
		for (Object element : set)
			if (areDeepEqual(element, object))
				return true;
		return false;
	}
}