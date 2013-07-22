package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.io.File;
import java.util.Random;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, KalibroSettings.class, Repository.class})
public class RepositoryTest extends UnitTest {

	private static final Long ID = Math.abs(new Random().nextLong());
	private static final Long PROJECT_ID = Math.abs(new Random().nextLong());

	private RepositoryDao dao;

	private Project project;
	private Configuration configuration;

	private Repository repository;

	private String formattedName;

	@Before
	public void setUp() {
		project = mock(Project.class);
		configuration = mock(Configuration.class);
		when(project.getName()).thenReturn("Project name");
		when(project.getId()).thenReturn(PROJECT_ID);

		repository = new Repository();
		repository.setConfiguration(configuration);
		repository.setProject(project);
		repository.setAddress("/");

		formattedName = "ProjectName-" + PROJECT_ID + "NewRepository-" + ID;
		mockDao();
	}

	private void mockDao() {
		dao = mock(RepositoryDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryDao()).thenReturn(dao);
		when(dao.save(repository, PROJECT_ID)).thenReturn(ID);
	}

	@Test
	public void shouldRetrieveSupportedTypes() {
		SortedSet<RepositoryType> supportedTypes = sortedSet(BAZAAR, GIT, MERCURIAL);
		when(dao.supportedTypes()).thenReturn(supportedTypes);
		assertSame(supportedTypes, Repository.supportedTypes());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(repository, withName(repository.getName()));
	}

	@Test
	public void checkConstruction() {
		repository = new Repository();
		assertFalse(repository.hasId());
		assertEquals("New repository", repository.getName());
		assertEquals("", repository.getDescription());
		assertEquals("", repository.getLicense());
		assertEquals("", repository.getAddress());
		assertEquals(RepositoryType.LOCAL_DIRECTORY, repository.getType());
		assertEquals(0, repository.getProcessPeriod().intValue());
		assertTrue(repository.historicProcessingIsDesired());
		assertNull(repository.getConfiguration());
		assertNull(repository.getProject());
	}

	@Test
	public void shouldGetCompleteName() {
		repository.setProject(null);
		assertEquals("New repository", repository.getCompleteName());

		repository.setProject(new Project());
		assertEquals("New project - New repository", repository.getCompleteName());
	}

	@Test
	public void shouldNotSetConflictingName() {
		project = new Project();
		project.addRepository(repository);
		project.addRepository(withName("name"));
		repository.setName("original");
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				repository.setName("name");
			}
		}).throwsException().withMessage("Repository named \"name\" already exists in the project.");
		assertEquals("original", repository.getName());
	}

	private Repository withName(String name) {
		return new Repository(name, RepositoryType.LOCAL_DIRECTORY, "");
	}

	@Test
	public void shouldAssertNoConflictWithOtherRepository() {
		repository.assertNoConflictWith(new Repository("a", RepositoryType.LOCAL_DIRECTORY, ""));
	}

	@Test
	public void shouldAssertSaved() {
		repository.assertSaved();
		verify(dao).save(repository, PROJECT_ID);

		repository.assertSaved();
		verifyNoMoreInteractions(dao);
	}

	@Test
	public void shouldRequireNameAddressProjectAndConfigurationToSave() {
		repository.setName(" ");
		assertSaveThrowsExceptionWithMessage("Repository requires name.");

		repository.setName("name");
		repository.setAddress(" ");
		assertSaveThrowsExceptionWithMessage("Repository requires address.");

		repository.setAddress("/");
		repository.setProject(null);
		assertSaveThrowsExceptionWithMessage("Repository is not in any project.");

		repository.setProject(project);
		repository.setConfiguration(null);
		assertSaveThrowsExceptionWithMessage("A configuration should be associated with the repository.");
	}

	private void assertSaveThrowsExceptionWithMessage(String message) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repository.save();
			}
		}).throwsException().withMessage(message);
	}

	@Test
	public void shouldAssertProjectAndConfigurationSavedBeforeSave() {
		repository.save();
		InOrder order = Mockito.inOrder(project, configuration, dao);
		order.verify(project).assertSaved();
		order.verify(configuration).assertSaved();
		order.verify(dao).save(repository, PROJECT_ID);
	}

	@Test
	public void shouldUpdateIdOnSave() {
		assertFalse(repository.hasId());
		repository.save();
		assertEquals(ID, repository.getId());
	}

	@Test
	public void shouldSaveBeforeProcess() {
		repository.process();
		InOrder order = Mockito.inOrder(dao);
		order.verify(dao).save(repository, PROJECT_ID);
		order.verify(dao).process(ID);
	}

	@Test
	public void shouldCancelProcessing() {
		Whitebox.setInternalState(repository, "id", ID);
		repository.cancelProcessing();
		verify(dao).cancelProcessing(ID);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		repository.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfSaved() {
		Whitebox.setInternalState(repository, "id", ID);

		assertTrue(repository.hasId());
		repository.delete();
		assertFalse(repository.hasId());
		verify(dao).delete(ID);
	}

	@Test
	public void shouldRemoveFromProjectOnDelete() {
		repository.delete();
		verify(project).removeRepository(repository);
	}

	@Test
	public void toStringShouldBeCompleteName() {
		assertEquals(repository.getCompleteName(), "" + repository);
	}

	@Test
	public void shouldVerifyDirectoryExistence() throws Exception {
		File loadDirectory = mockLoadDirectory();
		Whitebox.setInternalState(repository, "id", ID);

		File file = mock(File.class);
		whenNew(File.class).withArguments(loadDirectory, formattedName).thenReturn(file);
		when(file.isDirectory()).thenReturn(true);
		when(file.exists()).thenReturn(true);
		assertTrue(repository.hasBeenProcessedAtLeastOnce());
	}

	@Test
	public void shouldVerifyDirectoryInexistence() throws Exception {
		File loadDirectory = mockLoadDirectory();
		Whitebox.setInternalState(repository, "id", ID);

		File file = mock(File.class);
		whenNew(File.class).withArguments(loadDirectory, formattedName).thenReturn(file);
		when(file.exists()).thenReturn(false);
		assertFalse(repository.hasBeenProcessedAtLeastOnce());
	}

	private File mockLoadDirectory() {
		KalibroSettings kalibroSettings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		ServerSettings serverSettings = mock(ServerSettings.class);
		when(kalibroSettings.getServerSettings()).thenReturn(serverSettings);
		File loadDirectory = mock(File.class);
		when(serverSettings.getLoadDirectory()).thenReturn(loadDirectory);
		return loadDirectory;
	}
}