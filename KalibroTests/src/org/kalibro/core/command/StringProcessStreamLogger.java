package org.kalibro.core.command;

class StringProcessStreamLogger extends ProcessStreamLogger {

	private StringOutputStream outputStream = new StringOutputStream();
	private StringOutputStream errorStream = new StringOutputStream();

	@Override
	public void logOutputStream(Process process, String command) {
		pipe(process.getInputStream(), outputStream);
	}

	@Override
	public void logErrorStream(Process process, String command) {
		pipe(process.getErrorStream(), errorStream);
	}

	public String getStandardOuput() {
		return outputStream.getString();
	}

	public String getErrorOutput() {
		return errorStream.getString();
	}
}