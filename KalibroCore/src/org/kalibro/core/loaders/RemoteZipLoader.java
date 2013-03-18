package org.kalibro.core.loaders;

/**
 * Loader for remote zip files.
 * 
 * @author Carlos Morais
 */
public class RemoteZipLoader extends RemoteFileLoader {

	@Override
	protected FileLoader localLoader() {
		return new LocalZipLoader();
	}
}