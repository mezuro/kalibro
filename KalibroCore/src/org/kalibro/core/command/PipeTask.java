package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kalibro.core.concurrent.Task;

// TODO Verify if this can be replaced by use of java.io.PipedOutputStream and/or java.io.PipedInputStream
class PipeTask extends Task {

	private InputStream inputStream;
	private OutputStream outputStream;

	protected PipeTask(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public void perform() throws IOException {
		byte[] buffer = new byte[1024];
		for (int bytesRead = read(buffer); bytesRead != -1; bytesRead = read(buffer))
			outputStream.write(buffer, 0, bytesRead);
	}

	private int read(byte[] buffer) throws IOException {
		return inputStream.read(buffer);
	}
}