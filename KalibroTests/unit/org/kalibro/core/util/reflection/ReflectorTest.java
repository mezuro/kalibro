package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class ReflectorTest extends KalibroTestCase {

	private static final String INEXISTENT = "org.kalibro.core.util.reflection.ReflectorSample.inexistent";

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
	public void shouldAskForClassAnnotation() {
		assertTrue(reflector.hasClassAnnotation(Table.class));
		assertFalse(reflector.hasClassAnnotation(Test.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveClassAnnotation() {
		assertEquals("SAMPLE_TABLE", reflector.getClassAnnotation(Table.class).name());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAskForField() {
		assertTrue(reflector.hasField("id"));
		assertTrue(reflector.hasField("counter"));
		assertFalse(reflector.hasField("inexistent"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFields() {
		assertDeepEquals(reflector.listFields(), "counter", "description", "id", "name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterFields() {
		assertDeepEquals(reflector.listFields(isStatic()), "counter");
		assertDeepEquals(reflector.listFields(not(isStatic())), "description", "id", "name");
		assertDeepEquals(reflector.listFields(hasAnnotation(Id.class)), "id");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListFieldAnnotations() {
		List<Column> columns = reflector.getFieldAnnotations(Column.class);
		assertEquals(2, columns.size());
		assertEquals("description_column", columns.get(0).name());
		assertEquals("name_column", columns.get(1).name());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveFields() {
		assertEquals(ReflectorSample.count(), reflector.get("counter"));
		assertEquals(sample.getId(), reflector.get("id"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveParentFields() {
		assertEquals(sample.getName(), reflector.get("name"));
		assertEquals(sample.getDescription(), reflector.get("description"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetFields() {
		reflector.set("id", 42);
		assertEquals(42, sample.getId());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnExceptionRetrievingField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.get("inexistent");
			}
		}, "Error retrieving field: " + INEXISTENT, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnExceptionSettingField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.set("inexistent", "anything");
			}
		}, "Error setting field: " + INEXISTENT, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllMethods() {
		assertDeepEquals(reflector.listMethods(), "count", "getDescription", "getId", "getName", "setDescription");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterMethods() {
		assertDeepEquals(reflector.listMethods(isStatic()), "count");
		assertDeepEquals(reflector.listMethods(hasAnnotation(Basic.class)), "getId");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() {
		assertEquals(sample.getId(), reflector.invoke("getId"));
		reflector.invoke("setDescription", "sample for test");
		assertEquals("sample for test", sample.getDescription());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorOnExceptionInvokingMethod() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke("inexistent");
			}
		}, "Error invoking method: " + INEXISTENT, NullPointerException.class);
	}
}