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

	protected String getStandardOuput() {
		return outputStream.getString();
	}

	protected String getErrorOutput() {
		return errorStream.getString();
	}
}