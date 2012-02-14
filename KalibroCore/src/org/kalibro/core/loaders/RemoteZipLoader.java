package org.kalibro.core.loaders;

public class RemoteZipLoader extends RemoteFileLoader {

	@Override
	protected String getExtractorValidationCommand() {
		return "unzip -v";
	}

	@Override
	protected String getExtractionCommand(String temporaryFilePath, String loadPath) {
		return "unzip " + temporaryFilePath + " -d " + loadPath;
	}
}