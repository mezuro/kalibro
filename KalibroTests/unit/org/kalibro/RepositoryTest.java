package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryState.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class RepositoryTest extends UnitTest {

	private RepositoryDao dao;

	private Repository repository;

	@Before
	public void setUp() {
		dao = mock(RepositoryDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryDao()).thenReturn(dao);
		repository = new Repository();
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByNameAndId() {
		Repository other = withName(repository.getName());
		assertEquals(repository, other);
		Whitebox.setInternalState(other, "id", 1L);
		assertDifferent(repository, other);
	}

	@Test
	public void checkConstruction() {
		assertFalse(repository.hasId());
		assertEquals("New repository", repository.getName());
		assertEquals("", repository.getDescription());
		assertEquals("", repository.getLicense());
		assertEquals("", repository.getAddress());
		assertEquals(RepositoryType.LOCAL_DIRECTORY, repository.getType());
		assertNull(repository.getConfiguration());
		assertEquals(NEW, repository.getState());
	}

	@Test
	public void shouldGetCompleteName() {
		assertEquals("New repository", repository.getCompleteName());
		repository.setProject(new Project());
		assertEquals("New project - New repository", repository.getCompleteName());
	}

	@Test
	public void shouldNotSetConflictingName() {
		Project project = new Project();
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
	public void shouldBeInErrorStateAfterSettingError() {
		Throwable error = mock(Throwable.class);
		repository.setError(error);
		assertSame(error, repository.getError());
		assertEquals(ERROR, repository.getState());
	}

	@Test
	public void shouldGetStateMessageFromState() {
		assertEquals(NEW.getMessage(repository.getCompleteName()), repository.getStateMessage());
		repository.setState(ANALYZING);
		assertEquals(ANALYZING.getMessage(repository.getCompleteName()), repository.getStateMessage());
	}

	@Test
	public void shouldGetStateWhenErrorOcurred() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				repository.getStateWhenErrorOcurred();
			}
		}).throwsException().withMessage("Repository " + repository.getCompleteName() + " has no error.");
		repository.setState(ANALYZING);
		repository.setError(mock(Throwable.class));
		assertEquals(ANALYZING, repository.getStateWhenErrorOcurred());
	}

	@Test
	public void shouldNotAllowErrorStateWithoutException() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repository.setState(ERROR);
			}
		}).throwsException().withMessage("Use setError(Throwable) to put repository in error state");
	}

	@Test
	public void shouldRequireNameAddressSavedProjectAndConfigurationToSave() {
		saveShouldThrowExceptionWithMessage("Repository requires address.");

		repository.setAddress("/");
		repository.setName(" ");
		saveShouldThrowExceptionWithMessage("Repository requires name.");

		repository.setName("name");
		saveShouldThrowExceptionWithMessage("Repository is not in any project.");

		setProjectWithId(null);
		saveShouldThrowExceptionWithMessage("Project is not saved. Save project instead.");

		setProjectWithId(1L);
		saveShouldThrowExceptionWithMessage("A configuration should be associated with the repository.");

		setConfigurationWithId(null);
		saveShouldThrowExceptionWithMessage("The configuration associated with the repository is not saved.");
	}

	private void saveShouldThrowExceptionWithMessage(String message) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repository.save();
			}
		}).throwsException().withMessage(message);
	}

	@Test
	public void shouldUpdateIdOnSave() {
		Long id = prepareSave();
		assertFalse(repository.hasId());
		repository.save();
		assertSame(id, repository.getId());
	}

	@Test
	public void shouldProcess() {
		Long id = prepareSave();
		repository.process();
		verify(dao).process(id);
	}

	private Long prepareSave() {
		Long id = mock(Long.class);
		Long projectId = mock(Long.class);
		setProjectWithId(projectId);
		setConfigurationWithId(42L);
		repository.setAddress("/");
		when(dao.save(repository, projectId)).thenReturn(id);
		return id;
	}

	@Test
	public void shouldCancelProcessing() {
		Long id = mock(Long.class);
		Whitebox.setInternalState(repository, "id", id);
		repository.cancelProcessing();
		verify(dao).cancelProcessing(id);
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(repository.hasId());
		repository.delete();
		verify(dao, never()).delete(any(Long.class));

		Long id = mock(Long.class);
		Whitebox.setInternalState(repository, "id", id);

		assertTrue(repository.hasId());
		repository.delete();
		verify(dao).delete(id);
		assertFalse(repository.hasId());
	}

	@Test
	public void shouldRemoveFromprojectOnDelete() {
		Project project = setProjectWithId(42L);
		repository.delete();
		verify(project).removeRepository(repository);
	}

	private Project setProjectWithId(Long id) {
		Project project = mock(Project.class);
		when(project.hasId()).thenReturn(id != null);
		when(project.getId()).thenReturn(id);
		repository.setProject(project);
		return project;
	}

	private Configuration setConfigurationWithId(Long id) {
		Configuration configuration = mock(Configuration.class);
		when(configuration.hasId()).thenReturn(id != null);
		when(configuration.getId()).thenReturn(id);
		repository.setConfiguration(configuration);
		return configuration;
	}
}