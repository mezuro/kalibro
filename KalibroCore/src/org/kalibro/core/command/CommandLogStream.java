package org.kalibro.core.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.kalibro.core.Environment;

/**
 * A {@link OutputStream} that works as a proxy for a {@link FileOutputStream} that writes on the proper file in the
 * logs directory. The file name is based on the command to be logged and on the current date.
 * 
 * @author Carlos Morais
 */
class CommandLogStream extends OutputStream {

	private String command;
	private String fileExtension;
	private FileOutputStream fileStream;

	CommandLogStream(String command, String fileExtension) {
		this.command = command;
		this.fileExtension = fileExtension;
	}

	@Override
	public void write(int b) throws IOException {
		if (fileStream == null)
			createFileStream();
		fileStream.write(b);
	}

	private void createFileStream() throws IOException {
		fileStream = new FileOutputStream(getFile(), true);
		IOUtils.write("\n\n$ " + command + "\n", fileStream);
	}

	private File getFile() {
		String commandPrefix = command.split("\\s+")[0];
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String fileName = commandPrefix + "." + today + "." + fileExtension;
		return new File(Environment.logsDirectory(), fileName);
	}
}