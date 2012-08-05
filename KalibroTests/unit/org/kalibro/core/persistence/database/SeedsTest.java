package org.kalibro.core.persistence.database;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.mockito.Mockito;
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
		seededFile = mock(File.class);
		Whitebox.setInternalState(Seeds.class, seededFile);
		databaseManager = mock(DatabaseManager.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDoNothingIfSeededFileExists() {
		when(seededFile.exists()).thenReturn(true);
		Seeds.saveSeedsIfFirstTime(databaseManager);

		Mockito.verify(seededFile).exists();
		verifyNoMoreInteractions(seededFile, databaseManager);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSeedsIfFirstTime() {
		when(seededFile.exists()).thenReturn(false);
		Seeds.saveSeedsIfFirstTime(databaseManager);
		Mockito.verify(databaseManager).save(any(Collection.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateSeededFileAfterSaving() throws IOException {
		when(seededFile.exists()).thenReturn(false);
		Seeds.saveSeedsIfFirstTime(databaseManager);

		Mockito.verify(seededFile).createNewFile();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorCreatingSeededFile() throws IOException {
		when(seededFile.exists()).thenReturn(false);
		when(seededFile.createNewFile()).thenThrow(new IOException());
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				Seeds.saveSeedsIfFirstTime(databaseManager);
			}
		}, "Could not create file: " + seededFile, IOException.class);
	}
}