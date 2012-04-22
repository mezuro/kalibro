package org.kalibro.core.loaders;

import static org.kalibro.core.model.RepositoryFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.RepositoryFixtures;
import org.kalibro.core.model.enums.RepositoryType;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ProjectLoader.class)
public abstract class RemoteFileLoaderIntegrationTest extends LoaderIntegrationTest {

	@Before
	public void prepareTemporaryFile() throws Exception {
		CommandTask task = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(expectedDownloadCommand(), HELLO_WORLD_DIRECTORY).thenReturn(task);
		doAnswer(new TemporaryFilePreparation()).when(task).executeAndWait(ProjectLoader.LOAD_TIMEOUT);
	}

	private String expectedDownloadCommand() {
		Repository repository = RepositoryFixtures.helloWorldRepository(getRepositoryType());
		return "wget " + repository.getAddress() + " -O ./TEMP";
	}

	private class TemporaryFilePreparation implements Answer<Object> {

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			RepositoryType localType = RepositoryType.valueOf(getRepositoryType().name().replace("REMOTE", "LOCAL"));
			String localPath = helloWorldRepository(localType).getAddress();
			FileUtils.copyFile(new File(localPath), new File(HELLO_WORLD_DIRECTORY, "TEMP"));
			return null;
		}
	}
}