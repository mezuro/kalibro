package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.core.abstractentity.EqualityEvaluator.evaluate;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EqualityEvaluator.class)
public class EqualityEvaluatorTest extends KalibroTestCase {

	@Before
	public void setUp() {
		spy(EqualityEvaluator.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void canAlwaysEvaluate() {
		EqualityEvaluator<Object> evaluator = new EqualityEvaluator<Object>();
		assertTrue(evaluator.canEvaluate(null));
		assertTrue(evaluator.canEvaluate(this));
		assertTrue(evaluator.canEvaluate(""));
		assertTrue(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void nullShouldBeEqualNull() {
		assertTrue(evaluate(null, null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void onlyNullShouldBeEqualNull() {
		assertFalse(evaluate(this, null));
		assertFalse(evaluate(null, this));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void objectShouldBeEqualItself() {
		assertTrue(evaluate(this, this));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseSpecialCaseEvaluators() {
		FirstCharEvaluator firstCharEvaluator = new FirstCharEvaluator();
		assertTrue(evaluate("Equality", "Evaluator", firstCharEvaluator));
		assertFalse(evaluate("Evaluator", "Test", firstCharEvaluator));
		assertFalse(evaluate("Test", 42, firstCharEvaluator));
		assertTrue(evaluate(new Integer(42), new Integer(42), firstCharEvaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseEntityAsSpecialCaseOnNormalEquals() {
		when(EqualityEvaluator.evaluate(any(), any(), any(EqualityEvaluator.class))).thenReturn(true);
		EqualityEvaluator.areEqual(null, null);

		@SuppressWarnings("rawtypes")
		ArgumentCaptor<EqualityEvaluator> captor = ArgumentCaptor.forClass(EqualityEvaluator.class);
		verifyStatic();
		EqualityEvaluator.evaluate(eq(null), eq(null), captor.capture());
		assertClassEquals(EntityEqualityEvaluator.class, captor.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUseDeepEntityAsSpecialCaseOnDeepEquals() {
		when(EqualityEvaluator.evaluate(any(), any(), any(EqualityEvaluator.class))).thenReturn(true);
		EqualityEvaluator.areDeepEqual(null, null);

		@SuppressWarnings("rawtypes")
		ArgumentCaptor<EqualityEvaluator> captor = ArgumentCaptor.forClass(EqualityEvaluator.class);
		verifyStatic();
		EqualityEvaluator.evaluate(eq(null), eq(null), captor.capture());
		assertClassEquals(DeepEntityEqualityEvaluator.class, captor.getValue());
	}

	private class FirstCharEvaluator extends EqualityEvaluator<String> {

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