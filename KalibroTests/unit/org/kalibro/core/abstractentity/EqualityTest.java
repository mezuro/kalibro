package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class EqualityTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseNormalCasesOnNormalEquals() throws Exception {
		ArgumentCaptor<?> captor = spyEvaluate();
		Equality.areEqual(null, null);
		verifyEvaluate(captor, "NORMAL_CASES");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseDeepCasesOnDeepEquals() throws Exception {
		ArgumentCaptor<?> captor = spyEvaluate();
		Equality.areDeepEqual(null, null);
		verifyEvaluate(captor, "DEEP_CASES");
	}

	private ArgumentCaptor<?> spyEvaluate() throws Exception {
		spy(Equality.class);
		doReturn(true).when(Equality.class, "evaluate", any(), any(), any(Equality[].class));
		return ArgumentCaptor.forClass(Equality[].class);
	}

	private void verifyEvaluate(ArgumentCaptor<?> captor, String specialCases) throws Exception {
		verifyPrivate(Equality.class).invoke("evaluate", eq(null), eq(null), captor.capture());
		assertSame(Whitebox.getInternalState(Equality.class, specialCases), captor.getValue());
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