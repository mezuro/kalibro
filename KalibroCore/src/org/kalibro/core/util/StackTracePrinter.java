package org.kalibro.core.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StackTracePrinter extends ByteArrayOutputStream {

	private int maximumBytes;

	public StackTracePrinter() {
		this(Integer.MAX_VALUE);
	}

	public StackTracePrinter(int maximumBytes) {
		this.maximumBytes = maximumBytes;
	}

	public String printStackTrace(Exception exception) {
		reset();
		exception.printStackTrace(new PrintStream(this));
		return toString();
	}

	@Override
	public synchronized void write(byte[] bytes, int offset, int length) {
		int restrictedLength = Math.min(maximumBytes - count, length);
		if (restrictedLength > 0)
			super.write(bytes, offset, restrictedLength);
	}
}