package org.kalibro;

import static org.junit.Assert.*;

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
}