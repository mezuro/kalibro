package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class ListEqualityTest extends UnitTest {

	private static final boolean DEEP = new Random().nextBoolean();

	private ListEquality equality;

	@Before
	public void setUp() {
		equality = new ListEquality(DEEP);
		mockStatic(Equality.class);
		when(Equality.areEqual(any(), any(), eq(DEEP))).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test
	public void shouldEvaluateAnyTypeOfList() {
		assertTrue(equality.canEvaluate(new ArrayList<Object>()));
		assertTrue(equality.canEvaluate(new LinkedList<String>()));
		assertTrue(equality.canEvaluate(new Vector<ListEquality>(42)));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(new ArrayBlockingQueue<String>(42)));
	}

	@Test
	public void listsShouldHaveSameSize() {
		assertFalse(equality.equals(newList("1", "2"), newList("1", "2", "3")));
		assertFalse(equality.equals(newList("1", "2", "3"), newList("1", "2")));
	}

	@Test
	public void elementsShouldBeInTheSameOrder() {
		assertFalse(equality.equals(newList("1", "2"), newList("2", "1")));
		assertFalse(equality.equals(newList("1", "2", "3"), newList("1", "3", "2")));
	}

	@Test
	public void elementsShouldBeDeepEqualIfDeep() {
		assertTrue(equality.equals(newList("1", "2", "3"), newList("1", "2", "3")));
		verifyStatic();
		Equality.areEqual("1", "1", DEEP);
	}

	private List<String> newList(String... elements) {
		return list(elements);
	}
}