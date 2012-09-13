package org.kalibro.core.loaders;

import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;

public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Override
	@Test
	public void testLoad() {
		ProjectLoader loader = Whitebox.getInternalState(getRepositoryType(), ProjectLoader.class);
		assertThrowsException(new Task() {

			@Override
			public void perform() {
				load();
			}
		}, "Command returned with error status: " + loader.getLoadCommands(repository, false).get(0));
	}
}