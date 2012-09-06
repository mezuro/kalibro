package org.kalibro.core.util.reflection;

import static java.lang.reflect.Modifier.PRIVATE;
import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import javax.persistence.Column;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class FieldReflectorTest extends TestCase {

	private static final String INEXISTENT = "org.kalibro.core.util.reflection.FieldReflectorTest.inexistent";

	@Column(name = "self-fieldReflector")
	private FieldReflector fieldReflector;

	@Before
	public void setUp() {
		fieldReflector = new FieldReflector(this);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetObject() {
		assertSame(this, fieldReflector.getObject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetObjectClass() {
		assertEquals(getClass(), fieldReflector.getObjectClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListFields() {
		assertDeepList(fieldReflector.listFields(), "fieldReflector", "waiting");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterFields() {
		assertDeepList(fieldReflector.listFields(named("waiting")), "waiting");
		assertDeepList(fieldReflector.listFields(nameMatches(".*t.*")), "fieldReflector", "waiting");
		assertDeepList(fieldReflector.listFields(hasAnnotation(Column.class)), "fieldReflector");
		assertTrue(fieldReflector.listFields(not(is(PRIVATE))).isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetFieldAnnotation() {
		assertEquals("self-fieldReflector", fieldReflector.getFieldAnnotation("fieldReflector", Column.class).name());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetFieldValues() {
		assertSame(fieldReflector, fieldReflector.get("fieldReflector"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetFields() {
		assertFalse((Boolean) fieldReflector.get("waiting"));
		fieldReflector.set("waiting", true);
		assertTrue((Boolean) fieldReflector.get("waiting"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenGettingInexistentField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				fieldReflector.get("inexistent");
			}
		}, "Error retrieving field: " + INEXISTENT, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenSettingInexistentField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				fieldReflector.set("inexistent", "anything");
			}
		}, "Error setting field: " + INEXISTENT, NullPointerException.class);
	}
}