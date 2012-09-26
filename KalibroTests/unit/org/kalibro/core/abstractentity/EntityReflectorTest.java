package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class EntityReflectorTest extends UnitTest {

	private Person person;
	private Programmer programmer;
	private NoIdentityEntity noIdentityEntity;

	@Before
	public void setUp() {
		person = loadFixture("carlos", Person.class);
		programmer = loadFixture("carlos", Programmer.class);
		noIdentityEntity = new NoIdentityEntity();
	}

	@Test
	public void shouldListIdentityFields() {
		assertDeepEquals(asList("identityNumber"), reflector(person).listIdentityFields());
		assertDeepEquals(asList("identityNumber"), reflector(programmer).listIdentityFields());
	}

	@Test
	public void withoutSpecifyingAllFieldsShouldBeIdentityFields() {
		assertDeepEquals(asList("field1", "field2"), reflector(noIdentityEntity).listIdentityFields());
	}

	@Test
	public void shouldListSortingFields() {
		assertTrue(reflector(noIdentityEntity).listSortingFields().isEmpty());
		assertDeepEquals(asList("name"), reflector(person).listSortingFields());
		assertDeepEquals(asList("name"), reflector(programmer).listSortingFields());
	}

	@Test
	public void shouldListSortedPrintFields() {
		assertDeepEquals(asList("name", "identityNumber", "relatives", "sex"), reflector(person).listPrintFields());
		assertDeepEquals(asList("field1"), reflector(new NoIdentityEntity()).listPrintFields());
	}

	@Test
	public void shouldGetPrintComment() {
		assertEquals("", reflector(person).getPrintComment("identityNumber"));
		assertEquals("name comes first", reflector(person).getPrintComment("name"));
		assertEquals("", reflector(person).getPrintComment("relatives"));
	}

	private EntityReflector reflector(AbstractEntity<?> entity) {
		return new EntityReflector(entity);
	}
}