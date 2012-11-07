package org.kalibro.core.loaders;

public class RemoteTarballTest extends RemoteFileIntegrationTest {

	@Override
	protected String address() {
		return "http:/invalid.address/HelloWorld.tar.gz";
	}
}