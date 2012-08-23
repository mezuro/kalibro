package org.kalibro.core.abstractentity;

/**
 * Generic equality evaluator.
 * 
 * @author Carlos Morais
 */
abstract class Equality<T> {

	private static final Equality<?>[] NORMAL_CASES = new Equality[]{new EntityEquality()};
	private static final Equality<?>[] DEEP_CASES = new Equality[]{new DeepEntityEquality(), new ArrayEquality(),
		new ListEquality(), new MapEquality(), new SetEquality(), new StackTraceEquality(), new ThrowableEquality()};

	protected static boolean areEqual(Object value, Object other) {
		return evaluate(value, other, NORMAL_CASES);
	}

	protected static boolean areDeepEqual(Object value, Object other) {
		return evaluate(value, other, DEEP_CASES);
	}

	private static boolean evaluate(Object value, Object other, Equality<?>[] specialCases) {
		if (value == null)
			return other == null;
		if (other == null)
			return false;
		if (value == other)
			return true;
		return doEvaluate(value, other, specialCases);
	}

	private static boolean doEvaluate(Object value, Object other, Equality<?>[] specialCases) {
		for (Equality<?> equality : specialCases)
			if (equality.canEvaluate(value))
				return specialCase(equality, value, other);
		return new ObjectEquality().equals(value, other);
	}

	private static <T> boolean specialCase(Equality<T> equality, Object value, Object other) {
		try {
			return equality.equals((T) value, (T) other);
		} catch (ClassCastException exception) {
			return false;
		}
	}

	protected abstract boolean canEvaluate(Object value);

	protected abstract boolean equals(T value, T other);
}