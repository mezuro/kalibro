package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class ArrayEqualityTest extends TestCase {

	private ArrayEquality equality;

	@Before
	public void setUp() {
		equality = new ArrayEquality();
		mockStatic(Equality.class);
		when(Equality.areDeepEqual(any(), any())).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test(timeout = UNIT_TIMEOUT)
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

	@Test(timeout = UNIT_TIMEOUT)
	public void arraysShouldHaveSameSize() {
		assertFalse(equality.equals(newArray("1", "2"), newArray("1", "2", "3")));
		assertFalse(equality.equals(newArray("1", "2", "3"), newArray("1", "2")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeInTheSameOrder() {
		assertFalse(equality.equals(newArray("1", "2"), newArray("2", "1")));
		assertFalse(equality.equals(newArray("1", "2", "3"), newArray("1", "3", "2")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeDeepEqual() {
		assertTrue(equality.equals(newArray("1", "2", "3"), newArray("1", "2", "3")));
		verifyStatic();
		Equality.areDeepEqual("1", "1");
	}

	private String[] newArray(String... elements) {
		return elements;
	}
}