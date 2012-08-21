package org.kalibro.core.abstractentity;

import java.io.Serializable;

public abstract class AbstractEntity<T extends Comparable<? super T>> implements Comparable<T>, Serializable {

	@Override
	public String toString() {
		return EntityPrinter.print(this);
	}

	@Override
	public int hashCode() {
		return HashCodeCalculator.hash(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualityEvaluator.areEqual(this, other);
	}

	public boolean deepEquals(Object other) {
		return EqualityEvaluator.areDeepEqual(this, other);
	}

	@Override
	public int compareTo(T other) {
		return new EntityComparator<T>(this).compare(other);
	}
}