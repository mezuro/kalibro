package org.kalibro.core.model.abstracts;

import static org.junit.Assert.*;
import static org.kalibro.core.model.abstracts.PersonFixtures.*;
import static org.kalibro.core.model.abstracts.ProgrammerFixtures.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;

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
		assertSame(person, reflector.getEntity());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveEntityClass() {
		assertEquals(Person.class, reflector.getEntityClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFields() {
		assertDeepEquals(reflector.listAllFields(), "identityNumber", "name", "relatives", "sex");
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
	public void shouldThrowExceptionOnInexistentField() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.get("inexistentField");
			}
		}, "Field org.kalibro.core.model.abstracts.Person.inexistentField does not exist");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionOnBizarreInaccessibleField() {
		Map<String, Field> fields = Whitebox.getInternalState(reflector, Map.class);
		fields.get("name").setAccessible(false);
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.get("name");
			}
		}, "Field org.kalibro.core.model.abstracts.Person.name inaccessible", IllegalAccessException.class);
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
	public void shouldThrowExceptionOnInvalidSortingMethod() {
		String message = "Sorting method not found: org.kalibro.core.model.abstracts.InvalidSorting.invalid";
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				new EntityReflector(new InvalidSorting()).listSortingMethods();
			}
		}, message, NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() throws Exception {
		reflector = new EntityReflector(programmer);
		assertEquals(programmer.getIdentityNumber(), invokeMethod("getIdentityNumber"));
		assertEquals(programmer.getName(), invokeMethod("getName"));
		assertEquals(programmer.getSex(), invokeMethod("getSex"));
		assertEquals(programmer.getUseMetrics(), invokeMethod("getUseMetrics"));
	}

	private Object invokeMethod(String methodName) throws Exception {
		return reflector.invoke(reflector.getEntityClass().getMethod(methodName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionInvokingInaccessibleMethod() throws Exception {
		final Method method = Person.class.getDeclaredMethod("createRelatives");
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke(method);
			}
		}, "Method org.kalibro.core.model.abstracts.Person.createRelatives inaccessible", IllegalAccessException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionIfMethodThrowsKalibroException() throws Exception {
		reflector = new EntityReflector(new ExceptionEntity());
		final Method method = ExceptionEntity.class.getMethod("throwException");
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke(method);
			}
		}, "ExceptionEntity", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionForInvokationTargetException() throws Exception {
		reflector = new EntityReflector(new ExceptionEntity());
		final Method method = ExceptionEntity.class.getMethod("throwCause");
		String message = "Method org.kalibro.core.model.abstracts.ExceptionEntity.throwCause threw exception";
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke(method);
			}
		}, message, NullPointerException.class);
	}
}