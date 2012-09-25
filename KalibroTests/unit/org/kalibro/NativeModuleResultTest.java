package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.core.model.ModuleFixtures;
import org.kalibro.tests.UnitTest;

public class NativeModuleResultTest extends UnitTest {

	@Test
	public void checkInitialization() {
		Module module = ModuleFixtures.helloWorldApplication();
		NativeModuleResult result = new NativeModuleResult(module);
		assertSame(module, result.getModule());
		assertTrue(result.getMetricResults().isEmpty());
	}
}