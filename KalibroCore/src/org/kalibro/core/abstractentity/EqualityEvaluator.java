package org.kalibro.core.abstractentity;

/**
 * Determines equality of objects.
 * 
 * @author Carlos Morais
 */
class EqualityEvaluator<T> {

	protected static boolean areEqual(Object value, Object other) {
		return evaluate(value, other, new EntityEqualityEvaluator());
	}

	protected static boolean areDeepEqual(Object value, Object other) {
		return evaluate(value, other, new DeepEntityEqualityEvaluator(), new StackTraceElementEqualityEvaluator());
	}

	protected static boolean evaluate(Object value, Object other, EqualityEvaluator<?>... specialCaseEvaluators) {
		if (value == null)
			return other == null;
		if (other == null)
			return false;
		return (value == other) || doEvaluate(value, other, specialCaseEvaluators);
	}

	private static boolean doEvaluate(Object value, Object other, EqualityEvaluator<?>... specialCaseEvaluators) {
		for (EqualityEvaluator<?> evaluator : specialCaseEvaluators)
			if (evaluator.canEvaluate(value))
				return specialCase(evaluator, value, other);
		return new EqualityEvaluator<Object>().equals(value, other);
	}

	private static <T> boolean specialCase(EqualityEvaluator<T> evaluator, Object value, Object other) {
		try {
			return evaluator.equals((T) value, (T) other);
		} catch (ClassCastException exception) {
			return false;
		}
	}

	@SuppressWarnings("unused" /* parameter to be used by subclasses */)
	protected boolean canEvaluate(Object value) {
		return true;
	}

	protected boolean equals(T value, T other) {
		return value.equals(other);
	}
}