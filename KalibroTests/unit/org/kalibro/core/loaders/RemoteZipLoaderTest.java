package org.kalibro.core.loaders;

public class RemoteZipLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected Class<LocalZipLoader> expectedLocalLoader() {
		return LocalZipLoader.class;
	}
}