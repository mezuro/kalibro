package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class EntityComparatorTest extends TestCase {

	private static final String ASSERT_MESSAGE = "Order not satisfied: carlos, paulo";
	private static final String ERROR_MESSAGE = "Error comparing fields: org.kalibro.core.abstractentity.";

	private Person carlosPerson;
	private Programmer carlos, paulo;

	private EntityComparator<Person> comparator;

	@Before
	public void setUp() {
		carlosPerson = loadFixture("person-carlos", Person.class);
		carlos = loadFixture("programmer-carlos", Programmer.class);
		paulo = loadFixture("programmer-paulo", Programmer.class);
		comparator = new EntityComparator<Person>();
	}

	@Test
	public void compareShouldBeZeroWhenEntitiesAreEqual() {
		assertEquals(0, comparator.compare(carlos, carlos));
		assertEquals(0, comparator.compare(carlos, carlosPerson));
	}

	@Test
	public void compareShouldBeLessThanZeroWhenComparingToGreater() {
		assertTrue(ASSERT_MESSAGE, comparator.compare(carlos, paulo) < 0);
	}

	@Test
	public void compareShouldBeGreaterThanZeroWhenComparingToSmaller() {
		assertTrue(ASSERT_MESSAGE, comparator.compare(paulo, carlos) > 0);
	}

	@Test
	public void shouldThrowErrorWhenSortingFieldIsNull() {
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				comparator.compare(carlos, new Person("", null, ""));
			}
		}, ERROR_MESSAGE + "Programmer.name", InvocationTargetException.class);
	}

	@Test
	public void shouldThrowErrorWhenSortingFieldIsNotComparable() {
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				comparator.compare(new WeirdPerson(), new WeirdPerson());
			}
		}, ERROR_MESSAGE + "EntityComparatorTest$WeirdPerson.field", NoSuchMethodException.class);
	}

	@SortingFields("field")
	private class WeirdPerson extends Person {

		@SuppressWarnings("unused")
		private Object field = new String[0];
	}
}