package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class EntityReflectorTest extends KalibroTestCase {

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

	private EntityReflector reflector(AbstractEntity<?> entity) {
		return new EntityReflector(entity);
	}
}