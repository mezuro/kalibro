package org.kalibro.core;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;
import static org.kalibro.core.Environment.*;

import java.io.File;

import org.junit.After;
import org.junit.Test;
import org.kalibro.EnumerationTestCase;
import org.powermock.reflect.Whitebox;

public class EnvironmentTest extends EnumerationTestCase<Environment> {

	private static final File DOT_KALIBRO = new File(new File(System.getProperty("user.home")), ".kalibro");

	@After
	public void tearDown() {
		setEnvironment(TEST);
	}

	@Override
	protected Class<Environment> enumerationClass() {
		return Environment.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeInTestEnvironmentWhenTesting() {
		assertSame(TEST, getEnvironment());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAndRetrieveDotKalibroDirectory() {
		assertTrue(dotKalibro().exists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dotKalibroShouldBeAtHomeOnProdution() {
		setEnvironment(PRODUCTION);
		assertEquals(DOT_KALIBRO, dotKalibro());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dotKalibroShouldBeTestsDirectoryWhenTesting() {
		assertEquals("tests", dotKalibro().getName());
		assertEquals(DOT_KALIBRO, dotKalibro().getParentFile());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAndRetrieveLogsDirectory() {
		File logs = logsDirectory();
		assertTrue(logs.exists());
		assertEquals("logs", logs.getName());
		assertEquals(dotKalibro(), logs.getParentFile());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDropAndCreateDatabaseWhenTesting() {
		assertEquals(DROP_AND_CREATE, ddlGeneration());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOnlyCreateDatabaseWhenNotTesting() {
		setEnvironment(PRODUCTION);
		assertEquals(CREATE_ONLY, ddlGeneration());
	}

	private Environment getEnvironment() {
		return Whitebox.getInternalState(Environment.class, "current");
	}

	private void setEnvironment(Environment environment) {
		Whitebox.setInternalState(Environment.class, "current", environment);
	}
}