package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class EqualityTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseNormalCasesOnNormalEquals() throws Exception {
		spyEvaluate();
		Equality.areEqual(null, null);
		verifyPrivate(Equality.class).invoke("evaluate", eq(null), eq(null), isA(EntityEquality.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseDeepCasesOnDeepEquals() throws Exception {
		spyEvaluate();
		Equality.areDeepEqual(null, null);
		verifyPrivate(Equality.class).invoke("evaluate", eq(null), eq(null), isA(ArrayEquality.class),
			isA(DeepEntityEquality.class), isA(ListEquality.class), isA(MapEquality.class), isA(SetEquality.class),
			isA(StackTraceEquality.class), isA(ThrowableEquality.class));
	}

	private void spyEvaluate() throws Exception {
		spy(Equality.class);
		doReturn(true).when(Equality.class, "evaluate", any(), any(), anyVararg());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void nullShouldBeEqualNull() throws Exception {
		assertTrue(evaluate(null, null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void onlyNullShouldBeEqualNull() throws Exception {
		assertFalse(evaluate(this, null));
		assertFalse(evaluate(null, this));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void objectShouldBeEqualItself() throws Exception {
		assertTrue(evaluate(this, this));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseSpecialCaseEvaluators() throws Exception {
		FirstCharEvaluator firstCharEvaluator = new FirstCharEvaluator();
		assertFalse(evaluate("Equality", "Evaluator"));
		assertTrue(evaluate("Equality", "Evaluator", firstCharEvaluator));
		assertFalse(evaluate("Evaluator", "Test", firstCharEvaluator));
		assertFalse(evaluate("Test", 42, firstCharEvaluator));
		assertTrue(evaluate(42, 42, firstCharEvaluator));
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
}