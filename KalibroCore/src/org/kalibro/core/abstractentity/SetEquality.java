package org.kalibro.core.abstractentity;

import java.util.Set;

/**
 * Determines equality of sets. Two sets are equal if they contain the same elements.
 * 
 * @author Carlos Morais
 */
class SetEquality extends Equality<Set<?>> {

	private boolean deep;

	public SetEquality(boolean deep) {
		this.deep = deep;
	}

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
			if (areEqual(element, object, deep))
				return true;
		return false;
	}
}