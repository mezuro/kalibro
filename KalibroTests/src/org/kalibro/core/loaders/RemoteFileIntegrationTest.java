package org.kalibro.core.loaders;

import org.kalibro.KalibroError;
import org.kalibro.core.concurrent.VoidTask;

public abstract class RemoteFileIntegrationTest extends RepositoryIntegrationTest {

	@Override
	protected void load() {
		assertThat(whenLoading()).throwsException()
			.withMessage("Error while executing command: " + loader.loadCommands(address(), false).get(0));
		((RemoteFileLoader) loader).localLoader().load(localAddress(), projectsDirectory());
	}

	private VoidTask whenLoading() {
		return new VoidTask() {

			@Override
			protected void perform() {
				loader.load(address(), projectsDirectory());
			}
		};
	}

	private String localAddress() {
		String localTestName = getClass().getName().replace("Remote", "Local");
		try {
			Class<RepositoryIntegrationTest> testClass = (Class<RepositoryIntegrationTest>) Class
				.forName(localTestName);
			return testClass.getConstructor().newInstance().address();
		} catch (Exception exception) {
			throw new KalibroError("Local repository integration test not found: " + localTestName);
		}
	}
}