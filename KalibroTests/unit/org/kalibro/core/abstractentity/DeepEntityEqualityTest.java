package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class DeepEntityEqualityTest extends KalibroTestCase {

	private Person carlos, otherCarlos;
	private Programmer programmerCarlos;

	private DeepEntityEquality equality;

	@Before
	public void setUp() {
		carlos = loadFixture("person-carlos", Person.class);
		otherCarlos = loadFixture("person-carlos", Person.class);
		programmerCarlos = loadFixture("programmer-carlos", Programmer.class);
		equality = new DeepEntityEquality();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void classesShouldBeTheSame() {
		assertFalse(equalTo(programmerCarlos));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void allFieldsShouldBeEqual() {
		assertTrue(equalTo(otherCarlos));

		otherCarlos.setSex("M");
		assertFalse(equalTo(otherCarlos));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void allSubEntitiesShouldBeEqual() {
		assertTrue(equalTo(otherCarlos));

		otherCarlos.getRelatives().get("sister").setSex("F");
		assertFalse(equalTo(otherCarlos));
	}

	private boolean equalTo(AbstractEntity<?> other) {
		return equality.equals(carlos, other);
	}
}