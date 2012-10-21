package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class ArrayEqualityTest extends UnitTest {

	private static final boolean DEEP = new Random().nextBoolean();

	private ArrayEquality equality;

	@Before
	public void setUp() {
		equality = new ArrayEquality(DEEP);
		mockStatic(Equality.class);
		when(Equality.areEqual(any(), any(), eq(DEEP))).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test
	public void shouldEvaluateAnyTypeOfArray() {
		assertTrue(equality.canEvaluate(new Object[0]));
		assertTrue(equality.canEvaluate(new String[1]));
		assertTrue(equality.canEvaluate(new Test[2]));
		assertTrue(equality.canEvaluate(new Before[3]));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(equality));
		assertFalse(equality.canEvaluate(new ArrayList<String>()));
	}

	@Test
	public void arraysShouldHaveSameSize() {
		assertFalse(equality.equals(newArray("1", "2"), newArray("1", "2", "3")));
		assertFalse(equality.equals(newArray("1", "2", "3"), newArray("1", "2")));
	}

	@Test
	public void elementsShouldBeInTheSameOrder() {
		assertFalse(equality.equals(newArray("1", "2"), newArray("2", "1")));
		assertFalse(equality.equals(newArray("1", "2", "3"), newArray("1", "3", "2")));
	}

	@Test
	public void elementsShouldBeDeepEqualIfDeep() {
		assertTrue(equality.equals(newArray("1", "2", "3"), newArray("1", "2", "3")));
		verifyStatic();
		Equality.areEqual("1", "1", DEEP);
	}

	private String[] newArray(String... elements) {
		return elements;
	}
}