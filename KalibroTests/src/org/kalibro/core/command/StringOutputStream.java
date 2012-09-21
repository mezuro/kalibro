package org.kalibro.core.command;

import java.io.IOException;
import java.io.OutputStream;

class StringOutputStream extends OutputStream {

	private StringBuffer buffer = new StringBuffer();

	public String getString() {
		return buffer.toString();
	}

	@Override
	public void write(int b) throws IOException {
		buffer.append((char) b);
	}
}