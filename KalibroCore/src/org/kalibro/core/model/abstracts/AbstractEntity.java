package org.kalibro.core.model.abstracts;

import java.io.Serializable;

public abstract class AbstractEntity<T extends Comparable<? super T>> implements Comparable<T>, Serializable {

	@Override
	public String toString() {
		return new EntityPrinter(this).simplePrint();
	}

	public String deepPrint() {
		return new EntityPrinter(this).deepPrint();
	}

	@Override
	public int hashCode() {
		return new HashCodeCalculator(this).calculate();
	}

	@Override
	public boolean equals(Object other) {
		return new EqualityEvaluator(this).isEquals(other);
	}

	public boolean deepEquals(Object other) {
		return new DeepEqualityEvaluator(this).isEquals(other);
	}

	@Override
	public int compareTo(T other) {
		return new EntityComparator<T>(this).compare(other);
	}
}