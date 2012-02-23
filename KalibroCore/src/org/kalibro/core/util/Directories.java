package org.kalibro.core.util;

import java.io.File;

public final class Directories {

	public static File logs() {
		File logs = new File(kalibro(), "logs");
		logs.mkdirs();
		return logs;
	}

	public static File kalibro() {
		File kalibro = new File(home(), ".kalibro");
		kalibro.mkdirs();
		return kalibro;
	}

	private static File home() {
		return new File(System.getProperty("user.home"));
	}

	private Directories() {
		// Utility class
	}
}