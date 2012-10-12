package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.TreeSet;

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

	private SetEquality equality;

	@Before
	public void setUp() {
		equality = new SetEquality();
		mockStatic(Equality.class);
		when(Equality.areDeepEqual(any(), any())).thenAnswer(new EqualArgumentsAnswer());
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
		assertFalse(equality.equals(asSet(1, 2), asSet(1, 2, 3)));
		assertFalse(equality.equals(asSet(1, 2, 3), asSet(1, 2)));
	}

	@Test
	public void elementsShouldBeEqual() {
		assertFalse(equality.equals(asSet(6, 28), asSet(2, 42)));
		assertTrue(equality.equals(asSet(6, 28), asSet(6, 28)));
	}

	@Test
	public void elementsNeedNotToBeInTheSameOrder() {
		assertTrue(equality.equals(asSet(1, 2), asSet(2, 1)));
		assertTrue(equality.equals(asSet(1, 2, 3), asSet(2, 3, 1)));
	}

	@Test
	public void elementsShouldBeDeepEqual() {
		assertTrue(equality.equals(asSet(1, 2, 3), asSet(1, 2, 3)));
		verifyStatic();
		Equality.areDeepEqual(1, 1);
	}
}