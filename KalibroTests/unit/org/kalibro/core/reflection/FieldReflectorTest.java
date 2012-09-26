package org.kalibro.core.reflection;

import static java.lang.reflect.Modifier.PRIVATE;
import static org.junit.Assert.*;
import static org.kalibro.core.reflection.MemberFilterFactory.*;

import javax.persistence.Column;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class FieldReflectorTest extends UnitTest {

	private static final String INEXISTENT = "org.kalibro.core.reflection.FieldReflectorTest.inexistent";

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
		assertDeepEquals(asList("reflector", "testTimeout"), reflector.listFields());
	}

	@Test
	public void shouldFilterFields() {
		assertDeepEquals(asList("testTimeout"), reflector.listFields(named("testTimeout")));
		assertDeepEquals(asList("reflector"), reflector.listFields(nameMatches(".*lector")));
		assertDeepEquals(asList("reflector"), reflector.listFields(hasAnnotation(Column.class)));
		assertDeepEquals(asList("testTimeout"), reflector.listFields(not(is(PRIVATE))));
	}

	@Test
	public void shouldGetFieldType() {
		assertEquals(MethodRule.class, reflector.getFieldType("testTimeout"));
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
		assertSame(reflector, reflector.get("reflector"));
		reflector.set("reflector", null);
		assertNull(reflector);
	}

	@Test
	public void shouldThrowErrorWhenGettingInexistentField() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				reflector.get("inexistent");
			}
		}).throwsError().withMessage("Error retrieving field: " + INEXISTENT).withCause(NullPointerException.class);
	}

	@Test
	public void shouldThrowErrorWhenSettingInexistentField() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				reflector.set("inexistent", "anything");
			}
		}).throwsError().withMessage("Error setting field: " + INEXISTENT).withCause(NullPointerException.class);
	}
}