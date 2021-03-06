package org.kalibro.core.abstractentity;

/**
 * Determines equality of {@link Throwable}, deeply for stack trace and causes.
 * 
 * @author Carlos Morais
 */
class ThrowableEquality extends Equality<Throwable> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Throwable;
	}

	@Override
	protected boolean equals(Throwable throwable, Throwable other) {
		return areEqual(throwable.toString(), other.toString())
			&& areEqual(throwable.getMessage(), other.getMessage())
			&& areDeepEqual(throwable.getStackTrace(), other.getStackTrace())
			&& areDeepEqual(throwable.getCause(), other.getCause());
	}
}