package org.kalibro.util.reflection;

import static java.lang.reflect.Modifier.PRIVATE;
import static org.junit.Assert.*;
import static org.kalibro.util.reflection.MemberFilterFactory.*;

import javax.persistence.Column;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class FieldReflectorTest extends TestCase {

	private static final String INEXISTENT = "org.kalibro.util.reflection.FieldReflectorTest.inexistent";

	@Column(name = "self-reflector")
	private FieldReflector reflector;

	@Before
	public void setUp() {
		reflector = new FieldReflector(this);
	}

	@Test
	public void shouldGetObject() {
		assertSame(this, reflector.getObject());
	}

	@Test
	public void shouldGetObjectClass() {
		assertEquals(getClass(), reflector.getObjectClass());
	}

	@Test
	public void shouldListFields() {
		assertDeepList(reflector.listFields(), "reflector", "testTimeout", "waiting");
	}

	@Test
	public void shouldFilterFields() {
		assertDeepList(reflector.listFields(named("waiting")), "waiting");
		assertDeepList(reflector.listFields(nameMatches(".*t.*i.*")), "testTimeout", "waiting");
		assertDeepList(reflector.listFields(hasAnnotation(Column.class)), "reflector");
		assertDeepList(reflector.listFields(not(is(PRIVATE))), "testTimeout");
	}

	@Test
	public void shouldGetFieldType() {
		assertEquals(boolean.class, reflector.getFieldType("waiting"));
		assertEquals(FieldReflector.class, reflector.getFieldType("reflector"));
	}

	@Test
	public void shouldGetFieldAnnotation() {
		assertEquals("self-reflector", reflector.getFieldAnnotation("reflector", Column.class).name());
	}

	@Test
	public void shouldGetFieldValue() {
		assertSame(reflector, reflector.get("reflector"));
	}

	@Test
	public void shouldSetField() {
		assertFalse((Boolean) reflector.get("waiting"));
		reflector.set("waiting", true);
		assertTrue((Boolean) reflector.get("waiting"));
	}

	@Test
	public void shouldThrowErrorWhenGettingInexistentField() {
		assertThrowsError(new Task() {

			@Override
			public void perform() {
				reflector.get("inexistent");
			}
		}, "Error retrieving field: " + INEXISTENT, NullPointerException.class);
	}

	@Test
	public void shouldThrowErrorWhenSettingInexistentField() {
		assertThrowsError(new Task() {

			@Override
			public void perform() {
				reflector.set("inexistent", "anything");
			}
		}, "Error setting field: " + INEXISTENT, NullPointerException.class);
	}
}