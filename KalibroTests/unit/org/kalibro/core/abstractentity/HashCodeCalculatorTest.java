package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.abstractentity.HashCodeCalculator.hash;
import static org.kalibro.core.abstractentity.PersonFixtures.*;
import static org.kalibro.core.abstractentity.ProgrammerFixtures.*;

import org.junit.Test;
import org.kalibro.UtilityClassTest;

public class HashCodeCalculatorTest extends UtilityClassTest {

	@Override
	protected Class<?> utilityClass() {
		return HashCodeCalculator.class;
	}

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
}