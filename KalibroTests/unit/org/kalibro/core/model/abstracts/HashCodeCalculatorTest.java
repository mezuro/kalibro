package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class HashCodeCalculatorTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeDistributed() {
		assertDifferent(hash(carlos()), hash(cristina()), hash(isis()), hash(paulo()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeEqualForEqualObjects() {
		assertEquals(hash(carlos()), hash(programmerCarlos()));
		assertEquals(hash(paulo()), hash(programmerPaulo()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeEqualForNullIdentities() {
		Person fulano = new Person(null, "Fulano", "M");
		Person cicrana = new Person(null, "Cicrana", "F");
		assertEquals(hash(fulano), hash(cicrana));
	}

	private int hash(Person person) {
		return new HashCodeCalculator(person).calculate();
	}
}