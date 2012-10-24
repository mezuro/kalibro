package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.CLASS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricResultTest extends UnitTest {

	@Test
	public void checkConstruction() {
		NativeMetric metric = mock(NativeMetric.class);
		Double value = mock(Double.class);
		NativeMetricResult result = new NativeMetricResult(metric, value);
		assertSame(metric, result.getMetric());
		assertSame(value, result.getValue());
	}

	@Test
	public void shouldHaveDefaultConstructorForYamlLoading() throws Exception {
		Constructor<NativeMetricResult> constructor = NativeMetricResult.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		assertDeepEquals(new NativeMetricResult(new NativeMetric("", CLASS), Double.NaN), constructor.newInstance());
	}
}