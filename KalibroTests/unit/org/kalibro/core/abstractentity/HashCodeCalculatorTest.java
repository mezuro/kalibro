package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.abstractentity.HashCodeCalculator.hash;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.UtilityClassTest;

public class HashCodeCalculatorTest extends UtilityClassTest {

	private Person carlos, cristina, isis, paulo;
	private Programmer programmerCarlos, programmerPaulo;

	@Before
	public void setUp() {
		carlos = loadFixture("person-carlos", Person.class);
		cristina = loadFixture("person-cristina", Person.class);
		isis = loadFixture("person-isis", Person.class);
		paulo = loadFixture("person-paulo", Person.class);
		programmerCarlos = loadFixture("programmer-carlos", Programmer.class);
		programmerPaulo = loadFixture("programmer-paulo", Programmer.class);
	}

	@Override
	protected Class<?> utilityClass() {
		return HashCodeCalculator.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeDistributed() {
		assertDifferent(hash(carlos), hash(cristina), hash(isis), hash(paulo));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeEqualForEqualObjects() {
		assertEquals(hash(carlos), hash(programmerCarlos));
		assertEquals(hash(paulo), hash(programmerPaulo));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void hashCodesShouldBeEqualForNullIdentities() {
		Person fulano = new Person(null, "Fulano", "M");
		Person cicrana = new Person(null, "Cicrana", "F");
		assertEquals(hash(fulano), hash(cicrana));
	}
}