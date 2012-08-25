package org.kalibro.core.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.TestCase;

public class NativeModuleResultTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		Module module = ModuleFixtures.helloWorldApplication();
		NativeModuleResult result = new NativeModuleResult(module);
		assertSame(module, result.getModule());
		assertTrue(result.getMetricResults().isEmpty());
	}
}