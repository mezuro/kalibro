package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EntityComparatorTest extends KalibroTestCase {

	private Person person;
	private Programmer carlos, paulo;

	@Before
	public void setUp() {
		person = carlos();
		carlos = programmerCarlos();
		paulo = programmerPaulo();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSame() {
		assertNoOrder(person, person);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEquals() {
		assertNoOrder(person, carlos());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSmaller() {
		Person smaller = new Person("Z", "Ana Zélia", "Female");
		assertOrdered(smaller, person);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkGreater() {
		Person greater = new Person("A", "Zulu André", "Male");
		assertOrdered(person, greater);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSubclassCriteriaNotAppliedWhenComparingToSuperclass() {
		for (Boolean useMetrics : Arrays.asList(true, false)) {
			carlos.setUseMetrics(useMetrics);
			paulo.setUseMetrics(useMetrics);
			assertNoOrder(person, carlos);
			assertOrdered(person, paulo);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSubclassesSorting() {
		assertOrdered(carlos, paulo);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSubclassCanChangeSortingPriorities() {
		paulo.setUseMetrics(false);
		assertOrdered(paulo, carlos);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEntityWithNotComparableSortingField() {
		assertNoOrder(new SortingFieldNotComparable(), new SortingFieldNotComparable());
	}

	private <T extends Comparable<? super T>> void assertNoOrder(AbstractEntity<T> entity1, AbstractEntity<T> entity2) {
		assertEquals(0, new EntityComparator<T>(entity1).compare((T) entity2));
		assertEquals(0, new EntityComparator<T>(entity2).compare((T) entity1));
	}

	private <T extends Comparable<? super T>> void assertOrdered(AbstractEntity<T> first, AbstractEntity<T> second) {
		EntityComparator<T> firstComparator = new EntityComparator<T>(first);
		EntityComparator<T> secondComparator = new EntityComparator<T>(second);
		assertTrue(first + " was expected to come before " + second, firstComparator.compare((T) second) < 0);
		assertTrue(second + " was expected to come after " + first, secondComparator.compare((T) first) > 0);
	}

	@SortingMethods("getField")
	private class SortingFieldNotComparable extends AbstractEntity<SortingFieldNotComparable> {

		@SuppressWarnings("unused")
		public EntityComparatorTest getField() {
			return new EntityComparatorTest();
		}
	}
}