package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for local tarball files.
 * 
 * @author Carlos Morais
 */
public class LocalTarballLoader extends FileLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("tar --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		return Arrays.asList("tar -x --keep-newer-files -f " + address + " -C .");
	}
}