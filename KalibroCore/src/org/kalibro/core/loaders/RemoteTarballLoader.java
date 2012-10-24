package org.kalibro.core.loaders;

/**
 * Loader for remote tarball files.
 * 
 * @author Carlos Morais
 */
public class RemoteTarballLoader extends RemoteFileLoader {

	@Override
	protected RepositoryLoader localLoader() {
		return new LocalTarballLoader();
	}
}