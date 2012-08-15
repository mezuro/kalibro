package org.kalibro.core.command;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.kalibro.Environment;
import org.kalibro.KalibroException;

public class FileProcessStreamLogger extends ProcessStreamLogger {

	private Date date;
	private Process lastProcess;

	@Override
	public void logOutputStream(Process process, String command) {
		logStream(process, process.getInputStream(), command, "out");
	}

	@Override
	public void logErrorStream(Process process, String command) {
		logStream(process, process.getErrorStream(), command, "err");
	}

	private void logStream(Process process, InputStream inputStream, String command, String fileExtension) {
		File file = createFile(process, command, fileExtension);
		pipe(inputStream, createOuputStream(command, file));
	}

	private File createFile(Process process, String command, String fileExtension) {
		if (process != lastProcess) {
			date = new Date();
			lastProcess = process;
		}
		String firstCommandWord = command.split("\\s+")[0];
		String dateString = new SimpleDateFormat("yyyy-MM-dd_HH'h'mm'm'ss.SSS's'").format(date);
		return new File(Environment.logsDirectory(), firstCommandWord + "." + dateString + "." + fileExtension);
	}

	private OutputStream createOuputStream(String command, File file) {
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.write("$ " + command + "\n", outputStream);
			return outputStream;
		} catch (IOException exception) {
			throw new KalibroException("Error logging command: " + command + "\nFile: " + file, exception);
		}
	}
}