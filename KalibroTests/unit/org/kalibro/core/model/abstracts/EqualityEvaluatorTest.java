package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EqualityEvaluatorTest extends KalibroTestCase {

	private Person person;
	private EqualityEvaluator evaluator;

	@Before
	public void setUp() {
		person = carlos();
		evaluator = new EqualityEvaluator(person);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNull() {
		assertFalse(evaluator.isEquals(null));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptSelf() {
		assertTrue(evaluator.isEquals(person));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfNotAnEntity() {
		assertFalse(evaluator.isEquals(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfIdentityFieldsAreDifferent() {
		assertFalse(evaluator.isEquals(new NoIdentityEntity()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptIfIdentityValuesAreDifferent() {
		assertFalse(evaluator.isEquals(paulo()));

		Person different = carlos();
		different.setIdentityNumber("0");
		assertFalse(evaluator.isEquals(different));

		different.setIdentityNumber(null);
		assertFalse(evaluator.isEquals(different));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqual() {
		Person equalOther = carlos();
		assertNotSame(equalOther, person);
		assertTrue(evaluator.isEquals(equalOther));

		equalOther.setName("Another Name");
		assertTrue(evaluator.isEquals(equalOther));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqualSubclass() {
		assertTrue(evaluator.isEquals(programmerCarlos()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptEqualSuperclass() {
		evaluator = new EqualityEvaluator(programmerCarlos());
		assertTrue(evaluator.isEquals(person));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptOtherTypeIfHasSameIdentityFields() {
		assertTrue(evaluator.isEquals(new PersonImitation(person)));
	}
}