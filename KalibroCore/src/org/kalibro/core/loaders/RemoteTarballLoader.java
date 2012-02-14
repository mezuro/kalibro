package org.kalibro.core.loaders;

public class RemoteTarballLoader extends RemoteFileLoader {

	@Override
	protected String getExtractorValidationCommand() {
		return "tar --version";
	}

	@Override
	protected String getExtractionCommand(String temporaryFilePath, String loadPath) {
		return "tar -xf " + temporaryFilePath + " -C " + loadPath;
	}
}