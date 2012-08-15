package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Environment.*;

import org.junit.After;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class EnvironmentTest extends EnumerationTestCase<Environment> {

	private static final String KALIBRO_PATH = System.getProperty("user.home") + "/.kalibro";
	private static final String TESTS_PATH = KALIBRO_PATH + "/tests";

	@After
	public void tearDown() {
		setEnvironment(TEST);
	}

	@Override
	protected Class<Environment> enumerationClass() {
		return Environment.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShouldRunInTestEnvironment() {
		assertSame(TEST, getEnvironment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dotKalibroShouldBeTestsDirectoryIfNotProductionEnvironment() {
		for (Environment environment : Environment.values()) {
			setEnvironment(environment);
			String expected = (environment == PRODUCTION) ? KALIBRO_PATH : TESTS_PATH;
			assertEquals(expected, dotKalibro().getPath());
		}
	}

	private Environment getEnvironment() {
		return Whitebox.getInternalState(Environment.class, "current");
	}

	private void setEnvironment(Environment environment) {
		Whitebox.setInternalState(Environment.class, "current", environment);
	}
}