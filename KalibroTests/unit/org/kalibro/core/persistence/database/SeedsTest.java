package org.kalibro.core.persistence.database;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Seeds.class)
public class SeedsTest extends KalibroTestCase {

	private File seededFile;
	private DatabaseManager databaseManager;

	@Before
	public void setUp() {
		seededFile = PowerMockito.mock(File.class);
		Whitebox.setInternalState(Seeds.class, seededFile);
		databaseManager = PowerMockito.mock(DatabaseManager.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDoNothingIfSeededFileExists() {
		PowerMockito.when(seededFile.exists()).thenReturn(true);
		Seeds.saveSeedsIfFirstTime(databaseManager);

		verify(seededFile).exists();
		verifyNoMoreInteractions(seededFile, databaseManager);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSeedsIfFirstTime() {
		PowerMockito.when(seededFile.exists()).thenReturn(false);
		Seeds.saveSeedsIfFirstTime(databaseManager);

		verify(databaseManager, times(2)).save(any(List.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateSeededFileAfterSaving() throws IOException {
		PowerMockito.when(seededFile.exists()).thenReturn(false);
		Seeds.saveSeedsIfFirstTime(databaseManager);

		verify(seededFile).createNewFile();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorCreatingSeededFile() throws IOException {
		PowerMockito.when(seededFile.exists()).thenReturn(false);
		PowerMockito.when(seededFile.createNewFile()).thenThrow(new IOException("The error message"));
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				Seeds.saveSeedsIfFirstTime(databaseManager);
			}
		}, RuntimeException.class, "java.io.IOException: The error message", IOException.class);
	}
}