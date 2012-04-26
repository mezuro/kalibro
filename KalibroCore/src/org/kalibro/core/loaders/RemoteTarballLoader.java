package org.kalibro.core.loaders;

public class RemoteTarballLoader extends RemoteFileLoader {

	@Override
	protected ProjectLoader createLocalLoader() {
		return new LocalTarballLoader();
	}
}