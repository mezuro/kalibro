package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;

class StringInputStream extends InputStream {

	private int position;
	private String string;

	protected StringInputStream(String string) {
		position = 0;
		this.string = string;
	}

	@Override
	public int read() throws IOException {
		if (position >= string.length())
			return -1;
		return string.charAt(position++);
	}
}