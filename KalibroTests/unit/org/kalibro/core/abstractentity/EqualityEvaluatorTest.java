package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.core.abstractentity.PersonFixtures.*;
import static org.kalibro.core.abstractentity.ProgrammerFixtures.programmerCarlos;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EqualityEvaluatorTest extends KalibroTestCase {

	private Person person;

	@Before
	public void setUp() {
		person = carlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNull() {
		assertFalse(equalsTo(null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptSelf() {
		assertTrue(equalsTo(person));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfNotAnEntity() {
		assertFalse(equalsTo(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfIdentityFieldsAreDifferent() {
		assertFalse(equalsTo(new NoIdentityEntity()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfIdentityValuesAreDifferent() {
		assertFalse(equalsTo(paulo()));

		Person different = carlos();
		different.setIdentityNumber("0");
		assertFalse(equalsTo(different));

		different.setIdentityNumber(null);
		assertFalse(equalsTo(different));

		different.setIdentityNumber(person.getIdentityNumber());
		person.setIdentityNumber(null);
		assertFalse(equalsTo(different));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqual() {
		Person equalOther = carlos();
		assertNotSame(equalOther, person);
		assertTrue(equalsTo(equalOther));

		equalOther.setName("Another Name");
		assertTrue(equalsTo(equalOther));

		equalOther.setIdentityNumber(null);
		person.setIdentityNumber(null);
		assertTrue(equalsTo(equalOther));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqualSubclass() {
		assertTrue(equalsTo(programmerCarlos()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqualSuperclass() {
		assertTrue(new EqualityEvaluator(programmerCarlos(), person).areEqual());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptOtherTypeIfHasSameIdentityFields() {
		assertTrue(equalsTo(new PersonImitation(person)));
	}

	private boolean equalsTo(Object other) {
		return new EqualityEvaluator(person, other).areEqual();
	}
}