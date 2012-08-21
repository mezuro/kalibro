package org.kalibro.core.util.reflection;

import static java.lang.reflect.Modifier.PRIVATE;
import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.util.Comparator;

import javax.persistence.Column;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class ReflectorTest extends KalibroTestCase {

	private static final String INEXISTENT = "org.kalibro.core.util.reflection.ReflectorTest.inexistent";

	@Column(name = "self-reflector")
	private Reflector reflector;

	@Before
	public void setUp() {
		reflector = new Reflector(this);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetObject() {
		assertSame(this, reflector.getObject());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetObjectClass() {
		assertEquals(getClass(), reflector.getObjectClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListFields() {
		assertDeepEquals(reflector.listFields(), "waiting", "reflector");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilterFields() {
		assertDeepEquals(reflector.listFields(named("waiting")), "waiting");
		assertDeepEquals(reflector.listFields(nameMatches(".*t.*")), "waiting", "reflector");
		assertDeepEquals(reflector.listFields(hasAnnotation(Column.class)), "reflector");
		assertTrue(reflector.listFields(not(is(PRIVATE))).isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortFields() {
		assertDeepEquals(reflector.sortFields(thidLetterComparator()), "reflector", "waiting");
	}

	private Comparator<String> thidLetterComparator() {
		return new Comparator<String>() {

			@Override
			public int compare(String string1, String string2) {
				Character thirdLetter = new Character(string1.charAt(2));
				return thirdLetter.compareTo(string2.charAt(2));
			}
		};
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetFieldAnnotation() {
		assertEquals("self-reflector", reflector.getFieldAnnotation("reflector", Column.class).name());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetFieldValues() {
		assertSame(reflector, reflector.get("reflector"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetFields() {
		assertFalse((Boolean) reflector.get("waiting"));
		reflector.set("waiting", true);
		assertTrue((Boolean) reflector.get("waiting"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenGettingInexistentField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.get("inexistent");
			}
		}, "Error retrieving field: " + INEXISTENT, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenSettingInexistentField() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.set("inexistent", "anything");
			}
		}, "Error setting field: " + INEXISTENT, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokePublicMethodWithoutArgumentsByName() {
		assertEquals(hashCode(), reflector.invoke("hashCode"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenInvokingInexistentMethod() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("inexistent");
			}
		}, "Error invoking method: " + INEXISTENT, NoSuchMethodException.class);
	}
}