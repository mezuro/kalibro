package org.kalibro.core.command;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class PipeTaskTest extends UnitTest {

	private StringBuffer buffer = new StringBuffer();

	@Test
	public void shouldPipe() throws IOException {
		String string = "PipeTaskTest string";
		InputStream inputStream = IOUtils.toInputStream(string);
		OutputStream outputStream = new StringOutputStream();

		assertEquals("", buffer.toString());
		new PipeTask(inputStream, outputStream).perform();
		assertEquals(string, buffer.toString());
	}

	private class StringOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			buffer.append((char) b);
		}
	}
}