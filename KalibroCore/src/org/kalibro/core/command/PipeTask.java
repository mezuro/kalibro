package org.kalibro.core.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.kalibro.core.concurrent.VoidTask;

/**
 * Copy bytes from an {@link InputStream} to an {@link OutputStream}.
 * 
 * @author Carlos Morais
 */
class PipeTask extends VoidTask {

	private InputStream inputStream;
	private OutputStream outputStream;

	PipeTask(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	protected void perform() throws IOException {
		IOUtils.copy(inputStream, outputStream);
	}
}