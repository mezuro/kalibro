package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Repository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class RemoteFileLoaderTest extends KalibroTestCase {

	private static final String ADDRESS = "RemoteFileLoaderTest address";
	private static final String LOCAL_LOAD_COMMAND = "RemoteFileLoaderTest local load command";
	private static final String LOCAL_VALIDATION_COMMAND = "RemoteFileLoaderTest local validation command";

	private Repository repository;
	private ProjectLoader localLoader;

	private RemoteFileLoader remoteLoader;

	@Before
	public void setUp() {
		repository = new Repository(null, ADDRESS, "USERNAME", "PASSWORD");
		mockLocalLoader();
		remoteLoader = new FakeRemoteLoader();
	}

	private void mockLocalLoader() {
		localLoader = mock(ProjectLoader.class);
		when(localLoader.getValidationCommands()).thenReturn(Arrays.asList(LOCAL_VALIDATION_COMMAND));
		when(localLoader.getLoadCommands(any(Repository.class), anyBoolean()))
			.thenReturn(Arrays.asList(LOCAL_LOAD_COMMAND));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkValidationCommands() {
		assertDeepEquals(remoteLoader.getValidationCommands(), "wget --version", LOCAL_VALIDATION_COMMAND);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSupportAuthentication() {
		assertTrue(remoteLoader.supportsAuthentication());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkLoadCommands() {
		assertDeepEquals(remoteLoader.getLoadCommands(repository, false),
			"wget -N --user=USERNAME --password=PASSWORD " + ADDRESS + " -O " + temporaryFilePath(),
			LOCAL_LOAD_COMMAND);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkGetLocalLoadCommandsForTemporaryFile() {
		remoteLoader.getLoadCommands(repository, false);

		ArgumentCaptor<Repository> captor = ArgumentCaptor.forClass(Repository.class);
		Mockito.verify(localLoader).getLoadCommands(captor.capture(), eq(false));
		assertEquals(temporaryFilePath(), captor.getValue().getAddress());
	}

	private String temporaryFilePath() {
		return "./." + remoteLoader.hashCode();
	}

	private class FakeRemoteLoader extends RemoteFileLoader {

		@Override
		protected ProjectLoader createLocalLoader() {
			return localLoader;
		}
	}
}