package org.kalibro.core;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;

/**
 * Responsible for providing information which depends on the environment: production or test.
 * 
 * @author Carlos Morais
 */
public final class Environment {

	private static final File HOME = new File(System.getProperty("user.home"));
	private static final File DOT_KALIBRO = new File(HOME, ".kalibro");
	private static final File TESTS = new File(DOT_KALIBRO, "tests");

	public static File logsDirectory() {
		File logs = new File(dotKalibro(), "logs");
		logs.mkdirs();
		return logs;
	}

	public static File dotKalibro() {
		File dotKalibro = testing() ? TESTS : DOT_KALIBRO;
		dotKalibro.mkdirs();
		return dotKalibro;
	}

	public static String ddlGeneration() {
		return testing() ? DROP_AND_CREATE : CREATE_ONLY;
	}

	private static boolean testing() {
		return TESTS.exists();
	}

	private Environment() {
		return;
	}
}