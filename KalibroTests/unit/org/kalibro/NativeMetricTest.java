package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.Language.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricTest extends UnitTest {

	@Test
	public void checkConstruction() {
		NativeMetric metric = new NativeMetric("NativeMetricTest name", METHOD, JAVA, CPP);
		assertFalse(metric.isCompound());
		assertEquals("NativeMetricTest name", metric.getName());
		assertEquals(METHOD, metric.getScope());
		assertDeepEquals(set(JAVA, CPP), metric.getLanguages());
	}

	@Test
	public void shouldHaveDefaultConstructorForYamlLoading() throws Exception {
		Constructor<NativeMetric> constructor = NativeMetric.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		assertDeepEquals(new NativeMetric("", CLASS), constructor.newInstance());
	}
}