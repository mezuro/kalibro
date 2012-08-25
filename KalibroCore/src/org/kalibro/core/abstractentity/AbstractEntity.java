package org.kalibro.core.abstractentity;

import java.io.Serializable;

/**
 * This abstract class provides common methods to minimize the effort required to create an entity.<br/>
 * It implements overrides {@code toString()}, {@code hashCode()}, {@code equals()} and implements
 * {@code Comparable.compareTo()}.
 * 
 * @author Carlos Morais
 */
public abstract class AbstractEntity<T extends Comparable<? super T>> implements Comparable<T>, Serializable {

	@Override
	public String toString() {
		return Printer.print(this);
	}

	@Override
	public int hashCode() {
		return HashCodeCalculator.hash(this);
	}

	@Override
	public boolean equals(Object other) {
		return Equality.areEqual(this, other);
	}

	public boolean deepEquals(Object other) {
		return Equality.areDeepEqual(this, other);
	}

	@Override
	public int compareTo(T other) {
		return new EntityComparator<T>().compare(this, other);
	}
}