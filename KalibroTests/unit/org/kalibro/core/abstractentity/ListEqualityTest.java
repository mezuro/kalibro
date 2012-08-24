package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Equality.class)
public class ListEqualityTest extends KalibroTestCase {

	private ListEquality equality;

	@Before
	public void setUp() {
		equality = new ListEquality();
		mockStatic(Equality.class);
		when(Equality.areDeepEqual(any(), any())).thenAnswer(new EqualArgumentsAnswer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfList() {
		assertTrue(equality.canEvaluate(new ArrayList<Object>()));
		assertTrue(equality.canEvaluate(new LinkedList<String>()));
		assertTrue(equality.canEvaluate(new Vector<ListEquality>(42)));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(new ArrayBlockingQueue<String>(42)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void listsShouldHaveSameSize() {
		assertFalse(equality.equals(newList("1", "2"), newList("1", "2", "3")));
		assertFalse(equality.equals(newList("1", "2", "3"), newList("1", "2")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeInTheSameOrder() {
		assertFalse(equality.equals(newList("1", "2"), newList("2", "1")));
		assertFalse(equality.equals(newList("1", "2", "3"), newList("1", "3", "2")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void elementsShouldBeDeepEqual() {
		assertTrue(equality.equals(newList("1", "2", "3"), newList("1", "2", "3")));
		verifyStatic();
		Equality.areDeepEqual("1", "1");
	}

	private List<String> newList(String... elements) {
		return Arrays.asList(elements);
	}
}