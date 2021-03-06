package org.kalibro.core.abstractentity;

/**
 * Generic equality evaluator.
 * 
 * @author Carlos Morais
 */
public abstract class Equality<T> {

	public static boolean areEqual(Object value, Object other) {
		return areEqual(value, other, false);
	}

	public static boolean areDeepEqual(Object value, Object other) {
		return areEqual(value, other, true);
	}

	static boolean areEqual(Object value, Object other, boolean deep) {
		return evaluate(value, other, new ArrayEquality(deep), new EntityEquality(deep), new ListEquality(deep),
			new MapEquality(deep), new SetEquality(deep), new StackTraceEquality(), new ThrowableEquality());
	}

	private static boolean evaluate(Object value, Object other, Equality<?>... specialCases) {
		if (value == null)
			return other == null;
		if (other == null)
			return false;
		if (value == other)
			return true;
		return doEvaluate(value, other, specialCases);
	}

	private static boolean doEvaluate(Object value, Object other, Equality<?>... specialCases) {
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