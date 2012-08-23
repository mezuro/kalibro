package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Environment;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class SetEqualityTest extends KalibroTestCase {

	private SetEquality equality;

	@Before
	public void setUp() {
		equality = new SetEquality();
		mockStatic(Equality.class);
		when(Equality.areDeepEqual(any(), any())).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfSet() {
		assertTrue(equality.canEvaluate(new HashSet<Object>()));
		assertTrue(equality.canEvaluate(new TreeSet<String>()));
		assertTrue(equality.canEvaluate(EnumSet.allOf(Environment.class)));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(new ArrayList<String>()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void setsShouldHaveSameSize() {
		assertFalse(equality.equals(newSet(1, 2), newSet(1, 2, 3)));
		assertFalse(equality.equals(newSet(1, 2, 3), newSet(1, 2)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeEqual() {
		assertFalse(equality.equals(newSet(6, 28), newSet(2, 42)));
		assertTrue(equality.equals(newSet(6, 28), newSet(6, 28)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsNeedNotToBeInTheSameOrder() {
		assertTrue(equality.equals(newSet(1, 2), newSet(2, 1)));
		assertTrue(equality.equals(newSet(1, 2, 3), newSet(2, 3, 1)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeDeepEqual() {
		assertTrue(equality.equals(newSet(1, 2, 3), newSet(1, 2, 3)));
		verifyStatic();
		Equality.areDeepEqual(1, 1);
	}

	private Set<Integer> newSet(Integer... elements) {
		return new HashSet<Integer>(Arrays.asList(elements));
	}
}