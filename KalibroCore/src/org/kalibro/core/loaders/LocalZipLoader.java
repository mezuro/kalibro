package org.kalibro.core.loaders;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Loader for local zip files.
 * 
 * @author Carlos Morais
 */
public class LocalZipLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		return Arrays.asList("unzip -u -o " + address + " -d .");
	}

	@Override
	public boolean isUpdatable(File directory) {
		return false;
	}

	@Override
	protected String metadataDirectoryName() {
		return null;
	}
}