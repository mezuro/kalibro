package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for local zip files.
 * 
 * @author Carlos Morais
 */
public class LocalZipLoader extends FileLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		return Arrays.asList("unzip -u -o " + address + " -d .");
	}
}