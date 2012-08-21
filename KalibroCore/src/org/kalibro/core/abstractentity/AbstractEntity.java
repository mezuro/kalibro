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
		return new EntityEqualityEvaluator(this, other).areEqual();
	}

	public boolean deepEquals(Object other) {
		return new DeepEntityEqualityEvaluator(this, other).areEqual();
	}

	@Override
	public int compareTo(T other) {
		return new EntityComparator<T>(this).compare(other);
	}
}