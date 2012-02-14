package org.kalibro.core.command;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ProcessStreamLoggerTest extends KalibroTestCase {

	private static final long PIPE_TIMEOUT = 50;

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPipe() throws InterruptedException {
		StringInputStream input = new StringInputStream("My string");
		StringOutputStream output = new StringOutputStream();
		StringProcessStreamLogger logger = new StringProcessStreamLogger();

		logger.pipe(input, output);
		Thread.sleep(PIPE_TIMEOUT);
		assertEquals("My string", output.getString());
	}
}

class StringProcessStreamLogger extends ProcessStreamLogger {

	private StringOutputStream outputStream = new StringOutputStream();
	private StringOutputStream errorStream = new StringOutputStream();

	@Override
	public void logOutputStream(Process process, String command) {
		pipe(process.getInputStream(), outputStream);
	}

	@Override
	public void logErrorStream(Process process, String command) {
		pipe(process.getErrorStream(), errorStream);
	}

	public String getStandardOuput() {
		return outputStream.getString();
	}

	public String getErrorOutput() {
		return errorStream.getString();
	}
}