package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class EntityReflectorTest extends TestCase {

	private Person person;
	private Programmer programmer;
	private NoIdentityEntity noIdentityEntity;

	@Before
	public void setUp() {
		person = loadFixture("person-carlos", Person.class);
		programmer = loadFixture("programmer-carlos", Programmer.class);
		noIdentityEntity = new NoIdentityEntity();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListIdentityFields() {
		assertDeepList(reflector(person).listIdentityFields(), "identityNumber");
		assertDeepList(reflector(programmer).listIdentityFields(), "identityNumber");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void withoutSpecifyingAllFieldsShouldBeIdentityFields() {
		assertDeepList(reflector(noIdentityEntity).listIdentityFields(), "field1", "field2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListSortingFields() {
		assertTrue(reflector(noIdentityEntity).listSortingFields().isEmpty());
		assertDeepList(reflector(person).listSortingFields(), "name");
		assertDeepList(reflector(programmer).listSortingFields(), "name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListSortedPrintFields() {
		assertDeepList(reflector(person).listPrintFields(), "name", "identityNumber", "relatives", "sex");
		assertDeepList(reflector(new NoIdentityEntity()).listPrintFields(), "field1");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetPrintComment() {
		assertEquals("", reflector(person).getPrintComment("identityNumber"));
		assertEquals("name comes first", reflector(person).getPrintComment("name"));
		assertEquals("", reflector(person).getPrintComment("relatives"));
	}

	private EntityReflector reflector(AbstractEntity<?> entity) {
		return new EntityReflector(entity);
	}
}