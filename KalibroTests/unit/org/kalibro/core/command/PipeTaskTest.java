package org.kalibro.core.command;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.kalibro.TestCase;

public class PipeTaskTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPipe() throws IOException {
		String string = "Testing PipeTask";
		StringInputStream input = new StringInputStream(string);
		StringOutputStream output = new StringOutputStream();
		PipeTask task = new PipeTask(input, output);
		task.perform();
		assertEquals(string, output.getString());
	}
}