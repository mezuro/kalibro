package org.kalibro.core.loaders;

public class RemoteZipLoader extends RemoteFileLoader {

	@Override
	protected ProjectLoader createLocalLoader() {
		return new LocalZipLoader();
	}
}