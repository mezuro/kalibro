package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Id;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;

public class ReflectorTest extends KalibroTestCase {

	private ReflectorSample sample;

	private Reflector reflector;

	@Before
	public void setUp() {
		sample = new ReflectorSample("ReflectorTest name");
		reflector = new Reflector(sample);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveObject() {
		assertSame(sample, reflector.getObject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveEntityClass() {
		assertEquals(ReflectorSample.class, reflector.getObjectClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFields() {
		assertDeepEquals(reflector.listFields(), "counter", "description", "id", "name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterFields() {
		assertDeepEquals(reflector.listFields(isStatic()), "counter");
		assertDeepEquals(reflector.listFields(not(isStatic())), "description", "id", "name");
		assertDeepEquals(reflector.listFields(isAnnotatedWith(Id.class)), "id");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAccessFields() {
		assertEquals(ReflectorSample.count(), reflector.get("counter"));
		assertEquals(sample.getId(), reflector.get("id"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAccessParentFields() {
		assertEquals(sample.getName(), reflector.get("name"));
		assertEquals(sample.getDescription(), reflector.get("description"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnInexistentField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.get("inexistentField");
			}
		}, "Field org.kalibro.core.util.reflection.ReflectorSample.inexistentField does not exist");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnBizarreInaccessibleField() {
		Map<String, Field> fields = Whitebox.getInternalState(reflector, "fields");
		fields.get("name").setAccessible(false);
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.get("name");
			}
		}, "Field org.kalibro.core.util.reflection.ReflectorSample.name inaccessible", IllegalAccessException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllMethods() {
		assertDeepEquals(reflector.listMethods(), "count", "getDescription", "getId", "getName", "setDescription",
			"setId", "setName");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterMethods() {
		assertDeepEquals(reflector.listMethods(isStatic()), "count");
		assertDeepEquals(reflector.listMethods(not(isStatic())), "getDescription", "getId", "getName",
			"setDescription", "setId", "setName");
		assertDeepEquals(reflector.listMethods(isAnnotatedWith(Basic.class)), "getId");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() {
		assertEquals(sample.getId(), reflector.invoke("getId"));
		assertEquals(sample.getName(), reflector.invoke("getName"));
		assertEquals(sample.getDescription(), reflector.invoke("getDescription"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionOnError() {
		String message = "Error invoking method: org.kalibro.core.util.reflection.ReflectorSample.setId";
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke("setId");
			}
		}, message, IllegalArgumentException.class);
	}
}