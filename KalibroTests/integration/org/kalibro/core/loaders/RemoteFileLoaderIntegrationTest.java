package org.kalibro.core.loaders;

import org.kalibro.core.concurrent.VoidTask;
import org.powermock.reflect.Whitebox;

public abstract class RemoteFileLoaderIntegrationTest extends RepositoryIntegrationTest {

	@Override
	public void testLoad() {
		RepositoryLoader loader = Whitebox.getInternalState(getRepositoryType(), RepositoryLoader.class);
		assertThat(whenLoading()).throwsException()
			.withMessage("Command returned with error status: " + loader.loadCommands(repository, false).get(0));
	}

	private VoidTask whenLoading() {
		return new VoidTask() {

			@Override
			protected void perform() {
				load();
			}
		};
	}
}