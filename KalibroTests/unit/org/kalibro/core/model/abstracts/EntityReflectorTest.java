package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class EntityReflectorTest extends KalibroTestCase {

	private Person person;
	private Programmer programmer;
	private EntityReflector reflector;

	@Before
	public void setUp() {
		person = carlos();
		programmer = programmerCarlos();
		reflector = new EntityReflector(person);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkEntity() {
		assertSame(person, reflector.getEntity());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkEntityClass() {
		assertEquals(Person.class, reflector.getEntityClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAllFields() {
		assertDeepEquals(reflector.getAllFields(), "identityNumber", "name", "relatives", "sex");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkIdentityFields() {
		assertDeepEquals(reflector.getIdentityFields(), "identityNumber");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void noIdentityFieldsMeansAllIdentityFields() {
		reflector = new EntityReflector(new NoIdentityEntity());
		assertDeepEquals(reflector.getIdentityFields(), "field1", "field2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetSortingMethods() {
		verifySortingMethods(new NoIdentityEntity());
		verifySortingMethods(person, "getName");
		verifySortingMethods(programmer, "getUseMetrics", "getName");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testInvalidSortingMethod() {
		checkException(new Task() {

			@Override
			public void perform() {
				verifySortingMethods(new InvalidSorting());
			}
		}, RuntimeException.class, "Method not found", NoSuchMethodException.class);
	}

	private void verifySortingMethods(AbstractEntity<?> entity, String... expected) {
		reflector = new EntityReflector(entity);
		List<Method> sortingMethods = reflector.getSortingMethods();
		assertEquals(expected.length, sortingMethods.size());
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], sortingMethods.get(i).getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorGettingInexistentField() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				reflector.get("inexistentField");
			}
		}, IllegalArgumentException.class,
			"Field org.kalibro.core.model.abstracts.Person.inexistentField does not exist", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAccessFields() {
		assertEquals(person.getIdentityNumber(), reflector.get("identityNumber"));
		assertEquals(person.getName(), reflector.get("name"));
		assertEquals(person.getSex(), reflector.get("sex"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAccessParentFields() {
		reflector = new EntityReflector(programmer);
		assertEquals(person.getIdentityNumber(), reflector.get("identityNumber"));
		assertEquals(person.getName(), reflector.get("name"));
		assertEquals(person.getSex(), reflector.get("sex"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorInvokingInvalidMethod() throws Exception {
		final Method method = this.getClass().getMethod("setUp");
		checkException(new Task() {

			@Override
			public void perform() {
				reflector.invoke(method);
			}
		}, RuntimeException.class, "Error invoking method", IllegalArgumentException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() throws Exception {
		reflector = new EntityReflector(programmer);
		assertEquals(programmer.getIdentityNumber(), reflector.invoke(Programmer.class.getMethod("getIdentityNumber")));
		assertEquals(programmer.getName(), reflector.invoke(Programmer.class.getMethod("getName")));
		assertEquals(programmer.getSex(), reflector.invoke(Programmer.class.getMethod("getSex")));
		assertEquals(programmer.getUseMetrics(), reflector.invoke(Programmer.class.getMethod("getUseMetrics")));
	}
}