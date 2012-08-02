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
	public void shouldRetrieveEntity() {
		assertSame(person, reflector.getObject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveEntityClass() {
		assertEquals(Person.class, reflector.getObjectClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFields() {
		assertDeepEquals(reflector.listFields(), "identityNumber", "name", "relatives", "sex");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListIdentityFields() {
		assertDeepEquals(reflector.listIdentityFields(), "identityNumber");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFieldsAsIdentityIfEntityHasNoIdentityFields() {
		reflector = new EntityReflector(new NoIdentityEntity());
		assertDeepEquals(reflector.listIdentityFields(), "field1", "field2");
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
	public void shouldListSortingMethods() {
		verifySortingMethods(new NoIdentityEntity());
		verifySortingMethods(person, "getName");
		verifySortingMethods(programmer, "getUseMetrics", "getName");
	}

	private void verifySortingMethods(AbstractEntity<?> entity, String... expected) {
		reflector = new EntityReflector(entity);
		List<Method> sortingMethods = reflector.listSortingMethods();
		assertEquals(expected.length, sortingMethods.size());
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], sortingMethods.get(i).getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnInvalidSortingMethod() {
		String message = "Sorting method not found: org.kalibro.core.model.abstracts.InvalidSorting.invalid";
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				new EntityReflector(new InvalidSorting()).listSortingMethods();
			}
		}, message, NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() {
		reflector = new EntityReflector(programmer);
		assertEquals(programmer.getIdentityNumber(), invokeMethod("getIdentityNumber"));
		assertEquals(programmer.getName(), invokeMethod("getName"));
		assertEquals(programmer.getSex(), invokeMethod("getSex"));
		assertEquals(programmer.getUseMetrics(), invokeMethod("getUseMetrics"));
	}

	private Object invokeMethod(String methodName) {
		return reflector.invoke(methodName);
	}
}