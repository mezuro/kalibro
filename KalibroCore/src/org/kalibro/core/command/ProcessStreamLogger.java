package org.kalibro.core.command;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class ProcessStreamLogger {

	public abstract void logOutputStream(Process process, String command);

	public abstract void logErrorStream(Process process, String command);

	protected void pipe(InputStream input, OutputStream output) {
		new PipeTask(input, output).executeInBackground();
	}
}