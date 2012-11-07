package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class EqualityTest extends UnitTest {

	@Test
	public void shouldUseNormalCasesOnNormalEquals() throws Exception {
		spyAreEqual(false);
		Equality.areEqual(null, null);
		verifyPrivate(Equality.class).invoke("areEqual", null, null, false);
	}

	@Test
	public void shouldUseDeepCasesOnDeepEquals() throws Exception {
		spyAreEqual(true);
		Equality.areDeepEqual(null, null);
		verifyPrivate(Equality.class).invoke("areEqual", null, null, true);
	}

	private void spyAreEqual(boolean deep) throws Exception {
		spy(Equality.class);
		doReturn(true).when(Equality.class, "areEqual", null, null, deep);
	}

	@Test
	public void shouldUseSpecialCases() throws Exception {
		spy(Equality.class);
		doReturn(true).when(Equality.class, "evaluate", same(null), same(null), anyVararg());
		Equality.areEqual(null, null);
		verifyPrivate(Equality.class).invoke("evaluate", same(null), same(null), isA(ArrayEquality.class),
			isA(EntityEquality.class), isA(ListEquality.class), isA(MapEquality.class), isA(SetEquality.class),
			isA(StackTraceEquality.class), isA(ThrowableEquality.class));
	}

	@Test
	public void nullShouldBeEqualNull() throws Exception {
		assertTrue(evaluate(null, null));
	}

	@Test
	public void onlyNullShouldBeEqualNull() throws Exception {
		assertFalse(evaluate(this, null));
		assertFalse(evaluate(null, this));
	}

	@Test
	public void objectShouldBeEqualItself() throws Exception {
		assertTrue(evaluate(this, this));
	}

	@Test
	public void shouldUseSpecialCaseEvaluators() throws Exception {
		FirstCharEvaluator firstCharEvaluator = new FirstCharEvaluator();
		assertFalse(evaluate("Equality", "Evaluator"));
		assertTrue(evaluate("Equality", "Evaluator", firstCharEvaluator));
		assertFalse(evaluate("Evaluator", "Test", firstCharEvaluator));
		assertFalse(evaluate("Test", 42, firstCharEvaluator));
		assertTrue(evaluate(12, 42, firstCharEvaluator, new LastDigitEvaluator()));
	}

	private boolean evaluate(Object value, Object other, Equality<?>... specialCases) throws Exception {
		return Whitebox.invokeMethod(Equality.class, "evaluate", value, other, specialCases);
	}

	private class FirstCharEvaluator extends Equality<String> {

		@Override
		protected boolean canEvaluate(Object value) {
			return value instanceof String;
		}

		@Override
		protected boolean equals(String value, String other) {
			return value.charAt(0) == other.charAt(0);
		}
	}

	private class LastDigitEvaluator extends Equality<Integer> {

		@Override
		protected boolean canEvaluate(Object value) {
			return value instanceof Integer;
		}

		@Override
		protected boolean equals(Integer value, Integer other) {
			return (value % 10) == (other % 10);
		}
	}
}