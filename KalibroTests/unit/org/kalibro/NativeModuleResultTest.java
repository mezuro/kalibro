package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.CLASS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeModuleResultTest extends UnitTest {

	@Test
	public void checkConstruction() {
		Module module = mock(Module.class);
		NativeModuleResult result = new NativeModuleResult(module);
		assertSame(module, result.getModule());
		assertTrue(result.getMetricResults().isEmpty());
	}

	@Test
	public void shouldHaveDefaultConstructorForYamlLoading() throws Exception {
		Constructor<NativeModuleResult> constructor = NativeModuleResult.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		assertDeepEquals(new NativeModuleResult(new Module(CLASS)), constructor.newInstance());
	}
}