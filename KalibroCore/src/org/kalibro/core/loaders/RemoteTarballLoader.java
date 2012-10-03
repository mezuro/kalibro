package org.kalibro.core.loaders;

public class RemoteTarballLoader extends RemoteFileLoader {

	@Override
	protected RepositoryLoader createLocalLoader() {
		return new LocalTarballLoader();
	}
}