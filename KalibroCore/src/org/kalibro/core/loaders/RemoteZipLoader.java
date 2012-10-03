package org.kalibro.core.loaders;

public class RemoteZipLoader extends RemoteFileLoader {

	@Override
	protected RepositoryLoader createLocalLoader() {
		return new LocalZipLoader();
	}
}