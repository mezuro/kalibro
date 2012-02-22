package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kalibro.core.concurrent.Task;

class PipeTask extends Task {

	private InputStream inputStream;
	private OutputStream outputStream;

	protected PipeTask(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public void perform() throws IOException {
		int bytesRead;
		byte[] buffer = new byte[1024];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
	}
}