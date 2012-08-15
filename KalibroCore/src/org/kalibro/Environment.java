package org.kalibro;

import java.io.File;

import org.kalibro.core.util.Identifier;

/**
 * Responsible for providing information which depends on the environment: production, development or test.
 * 
 * @author Carlos Morais
 */
public enum Environment {

	TEST, DEVELOPMENT, PRODUCTION;

	private static Environment current = PRODUCTION;

	public static File dotKalibro() {
		File home = new File(System.getProperty("user.home"));
		File dotKalibro = new File(home, ".kalibro");
		if (current != PRODUCTION)
			dotKalibro = new File(dotKalibro, "tests");
		dotKalibro.mkdirs();
		return dotKalibro;
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}
}