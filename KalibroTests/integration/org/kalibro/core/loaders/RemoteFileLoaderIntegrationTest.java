package org.kalibro.core.loaders;

import org.junit.Test;
import org.kalibro.core.concurrent.Task;

public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Override
	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLoad() {
		String errorCommand = "wget --user=USERNAME --password=PASSWORD " + repository.getAddress() + " -O ./TEMP";
		checkKalibroException(new Task() {

			@Override
			protected void perform() {
				load();
			}
		}, "Command returned with error status: " + errorCommand);
	}
}