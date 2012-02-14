package org.kalibro.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class StackTracePrinter {

	private static final int MAX_STACK_TRACE_LENGTH = 1000;

	private Exception exception;
	private StringBuffer buffer;

	public StackTracePrinter(Exception exception) {
		this.exception = exception;
	}

	public String printStackTrace() {
		buffer = new StringBuffer();
		exception.printStackTrace(new PrintStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				if (buffer.length() < MAX_STACK_TRACE_LENGTH)
					buffer.append((char) b);
			}
		}));
		return buffer.toString();
	}
}