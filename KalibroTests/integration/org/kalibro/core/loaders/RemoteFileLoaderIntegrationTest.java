package org.kalibro.core.loaders;

import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;

public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Override
	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLoad() {
		ProjectLoader loader = Whitebox.getInternalState(getRepositoryType(), ProjectLoader.class);
		checkKalibroException(new Task() {

			@Override
			protected void perform() {
				load();
			}
		}, "Command returned with error status: " + loader.getLoadCommands(repository, false).get(0));
	}
}