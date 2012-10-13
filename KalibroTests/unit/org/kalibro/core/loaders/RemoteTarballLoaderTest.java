package org.kalibro.core.loaders;

public class RemoteTarballLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected Class<LocalTarballLoader> expectedLocalLoader() {
		return LocalTarballLoader.class;
	}
}