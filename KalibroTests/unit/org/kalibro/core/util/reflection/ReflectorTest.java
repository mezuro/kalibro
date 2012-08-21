package org.kalibro.core.util.reflection;

import static java.lang.reflect.Modifier.STATIC;
import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.util.Comparator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;

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
	public void shouldGetObject() {
		assertSame(sample, reflector.getObject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetObjectClass() {
		assertEquals(sample.getClass(), reflector.getObjectClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllFields() {
		assertDeepEquals(reflector.listFields(), "counter", "description", "id", "name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterFields() {
		assertDeepEquals(reflector.listFields(is(STATIC)), "counter");
		assertDeepEquals(reflector.listFields(not(is(STATIC))), "description", "id", "name");
		assertDeepEquals(reflector.listFields(hasAnnotation(Id.class)), "id");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortFields() {
		Comparator<String> comparator = new Comparator<String>() {

			@Override
			public int compare(String fieldName1, String fieldName2) {
				String reverse1 = new StringBuffer(fieldName1).reverse().toString();
				String reverse2 = new StringBuffer(fieldName2).reverse().toString();
				return reverse1.compareTo(reverse2);
			}
		};
		assertDeepEquals(reflector.sortFields(comparator), "id", "name", "description", "counter");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetFieldAnnotation() {
		assertEquals("name_column", reflector.getFieldAnnotation("name", Column.class).name());
		assertEquals("description_column", reflector.getFieldAnnotation("description", Column.class).name());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveFieldValues() {
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
		assertDeepEquals(reflector.listMethods(is(STATIC)), "count");
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