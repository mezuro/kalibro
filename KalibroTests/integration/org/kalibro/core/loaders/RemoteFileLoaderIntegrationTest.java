package org.kalibro.core.loaders;

import org.kalibro.core.concurrent.VoidTask;
import org.powermock.reflect.Whitebox;

public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Override
	public void testLoad() {
		ProjectLoader loader = Whitebox.getInternalState(getRepositoryType(), ProjectLoader.class);
		assertThat(whenLoading()).throwsException()
			.withMessage("Command returned with error status: " + loader.getLoadCommands(repository, false).get(0));
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