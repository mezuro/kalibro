package org.kalibro.core.loaders;

import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;

public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Override
	public void testLoad() {
		ProjectLoader loader = Whitebox.getInternalState(getRepositoryType(), ProjectLoader.class);
		assertThat(whenLoading()).throwsException()
			.withMessage("Command returned with error status: " + loader.getLoadCommands(repository, false).get(0));
	}

	private Task whenLoading() {
		return new Task() {

			@Override
			public void perform() {
				load();
			}
		};
	}
}