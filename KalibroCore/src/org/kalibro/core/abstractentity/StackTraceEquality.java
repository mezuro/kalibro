package org.kalibro.core.abstractentity;

/**
 * Determines equality of stack trace elements.
 * 
 * @author Carlos Morais
 */
class StackTraceEquality extends Equality<StackTraceElement> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof StackTraceElement;
	}

	@Override
	protected boolean equals(StackTraceElement value, StackTraceElement other) {
		return areEqual(value.getClassName(), other.getClassName())
			&& areEqual(value.getMethodName(), other.getMethodName())
			&& areEqual(value.getFileName(), other.getFileName())
			&& areEqual(value.getLineNumber(), other.getLineNumber());
	}
}