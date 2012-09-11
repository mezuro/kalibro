package org.kalibro.core;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;

import org.kalibro.util.Identifier;

/**
 * Responsible for providing information which depends on the environment: production or test.
 * 
 * @author Carlos Morais
 */
public enum Environment {

	TEST, PRODUCTION;

	private static Environment current = PRODUCTION;

	public static File dotKalibro() {
		File home = new File(System.getProperty("user.home"));
		File dotKalibro = new File(home, ".kalibro");
		if (current == TEST)
			dotKalibro = new File(dotKalibro, "tests");
		dotKalibro.mkdirs();
		return dotKalibro;
	}

	public static File logsDirectory() {
		File logs = new File(dotKalibro(), "logs");
		logs.mkdirs();
		return logs;
	}

	public static String ddlGeneration() {
		return current == TEST ? DROP_AND_CREATE : CREATE_ONLY;
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}
}