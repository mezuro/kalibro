package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.core.abstractentity.PersonFixtures.*;
import static org.kalibro.core.abstractentity.ProgrammerFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AbstractEntityTest extends KalibroTestCase {

	private Person person;
	private Programmer programmer;

	@Before
	public void setUp() {
		person = carlos();
		programmer = programmerCarlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals(EntityPrinter.print(person), "" + person);
		assertEquals(EntityPrinter.print(programmer), "" + programmer);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHashCode() {
		assertEquals(new HashCodeCalculator(person).calculate(), person.hashCode());
		assertEquals(new HashCodeCalculator(programmer).calculate(), programmer.hashCode());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEquals() {
		assertEquals(person, person);
		assertEquals(person, carlos());
		assertEquals(person, programmer);
		assertEquals(person, new PersonImitation(person));

		assertFalse(person.equals(programmerPaulo()));
		assertFalse(person.equals("person"));
		assertFalse(person.equals(null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDeepEquals() {
		assertDeepEquals(person, person);
		assertDeepEquals(person, carlos());

		assertFalse(person.deepEquals(programmer));
		assertFalse(programmer.deepEquals(person));

		Person almostEquals = carlos();
		almostEquals.getRelatives().get("sister").setName("Isis Nascimento de Oliveira");
		assertFalse(person.deepEquals(almostEquals));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSorting() {
		assertSorted(new Programmer(cristina(), false), new Programmer(isis(), false), programmer, programmerPaulo());
	}
}