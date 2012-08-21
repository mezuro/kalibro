package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;
import static org.kalibro.core.abstractentity.PersonFixtures.*;
import static org.kalibro.core.abstractentity.ProgrammerFixtures.programmerCarlos;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EntityEqualityEvaluatorTest extends KalibroTestCase {

	private Person carlos;

	@Before
	public void setUp() {
		carlos = carlos();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotBeEqualToEntityWithDifferentIdentityFields() {
		assertFalse(equalTo(new NoIdentityEntity()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotBeEqualToEntityWithDifferentIdentityValues() {
		assertFalse(equalTo(paulo()));
		assertFalse(equalTo(withIdentity("0")));
		assertFalse(equalTo(withIdentity(null)));

		carlos.setIdentityNumber(null);
		assertFalse(equalTo(carlos()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeEqualToEntityWithEqualIdentityValues() {
		assertTrue(equalTo(carlos()));
		assertTrue(equalTo(withName("Another Name")));

		carlos.setIdentityNumber(null);
		assertTrue(equalTo(withIdentity(null)));
	}

	private Person withIdentity(String identity) {
		Person different = carlos();
		different.setIdentityNumber(identity);
		return different;
	}

	private Person withName(String name) {
		Person different = carlos();
		different.setName(name);
		return different;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeEqualToEntityOfOtherClassWithEqualIdentityValues() {
		assertTrue(equalTo(programmerCarlos()));
		assertTrue(new EntityEqualityEvaluator().equals(programmerCarlos(), carlos));
		assertTrue(equalTo(new PersonImitation(carlos)));
	}

	private boolean equalTo(AbstractEntity<?> other) {
		return new EntityEqualityEvaluator().equals(carlos, other);
	}
}