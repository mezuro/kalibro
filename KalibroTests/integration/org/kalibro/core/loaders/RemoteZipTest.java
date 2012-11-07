package org.kalibro.core.loaders;

public class RemoteZipTest extends RemoteFileIntegrationTest {

	@Override
	protected String address() {
		return "http://invalid.address/HelloWorld.zip";
	}
}