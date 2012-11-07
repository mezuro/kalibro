package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Language;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class SetEqualityTest extends UnitTest {

	private static final boolean DEEP = new Random().nextBoolean();

	private SetEquality equality;

	@Before
	public void setUp() {
		equality = new SetEquality(DEEP);
		mockStatic(Equality.class);
		when(Equality.areEqual(any(), any(), eq(DEEP))).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test
	public void shouldEvaluateAnyTypeOfSet() {
		assertTrue(equality.canEvaluate(new HashSet<Object>()));
		assertTrue(equality.canEvaluate(new TreeSet<String>()));
		assertTrue(equality.canEvaluate(EnumSet.allOf(Language.class)));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(new ArrayList<String>()));
	}

	@Test
	public void setsShouldHaveSameSize() {
		assertFalse(equality.equals(set(1, 2), set(1, 2, 3)));
		assertFalse(equality.equals(set(1, 2, 3), set(1, 2)));
	}

	@Test
	public void elementsShouldBeEqual() {
		assertFalse(equality.equals(set(6, 28), set(2, 42)));
		assertTrue(equality.equals(set(6, 28), set(6, 28)));
	}

	@Test
	public void elementsNeedNotToBeInTheSameOrder() {
		assertTrue(equality.equals(set(1, 2), set(2, 1)));
		assertTrue(equality.equals(set(1, 2, 3), set(2, 3, 1)));
	}

	@Test
	public void elementsShouldBeDeepEqualIfDeep() {
		assertTrue(equality.equals(set(1, 2, 3), set(1, 2, 3)));
		verifyStatic();
		Equality.areEqual(1, 1, DEEP);
	}
}