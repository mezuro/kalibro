package org.kalibro.core;

import org.junit.Test;
import org.kalibro.TestCase;

public class KalibroFacadeTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void constructorCoverage() {
		new DummyFacade();
	}
}