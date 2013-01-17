package org.kalibro.core.loaders;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Loader for local directories.
 * 
 * @author Carlos Morais
 */
public class LocalDirectoryLoader extends FileLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("cp --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		return Arrays.asList("cp -ru " + address + " .");
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