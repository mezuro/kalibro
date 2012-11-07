package org.kalibro.core;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;
import static org.junit.Assert.*;
import static org.kalibro.core.Environment.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UtilityClassTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Environment.class)
public class EnvironmentTest extends UtilityClassTest {

	private static final File HOME = new File(System.getProperty("user.home"));
	private static final File DOT_KALIBRO = new File(HOME, ".kalibro");
	private static final File TESTS = new File(DOT_KALIBRO, "tests");

	@Before
	public void setUp() throws Exception {
		spy(Environment.class);
		assertTrue(isTesting());
	}

	private boolean isTesting() throws Exception {
		return Whitebox.invokeMethod(Environment.class, "testing");
	}

	@Override
	protected Class<Environment> utilityClass() {
		return Environment.class;
	}

	@Test
	public void shouldGetDotKalibroDirectory() throws Exception {
		assertEquals(TESTS, dotKalibro());
		setProduction();
		assertEquals(DOT_KALIBRO, dotKalibro());
	}

	@Test
	public void shouldGetLogsDirectory() throws Exception {
		assertEquals(new File(TESTS, "logs"), logsDirectory());
		setProduction();
		assertEquals(new File(DOT_KALIBRO, "logs"), logsDirectory());
	}

	@Test
	public void shouldGetDdlGeneration() throws Exception {
		assertEquals(DROP_AND_CREATE, ddlGeneration());
		setProduction();
		assertEquals(CREATE_ONLY, ddlGeneration());
	}

	private void setProduction() throws Exception {
		doReturn(false).when(Environment.class, "testing");
	}
}