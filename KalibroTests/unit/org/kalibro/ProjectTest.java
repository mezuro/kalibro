package org.kalibro;

import static org.junit.Assert.*;

import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProjectTest extends UnitTest {

	private ProjectDao dao;

	private Project project;

	@Before
	public void setUp() {
		dao = mock(ProjectDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(dao);
		project = new Project();
	}

	@Test
	public void shouldGetAllProjects() {
		SortedSet<Project> projects = mock(SortedSet.class);
		when(dao.all()).thenReturn(projects);
		assertSame(projects, Project.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(project, withName(project.getName()));
	}

	private Project withName(String name) {
		return new Project(name);
	}

	@Test
	public void checkConstruction() {
		assertFalse(project.hasId());
		assertEquals("New project", project.getName());
		assertEquals("", project.getDescription());
		assertTrue(project.getRepositories().isEmpty());
	}

	@Test
	public void shouldSetProjectOnRepositories() {
		Repository repository = mock(Repository.class);
		project.setRepositories(sortedSet(repository));
		assertDeepEquals(set(repository), project.getRepositories());
		verify(repository).setProject(project);
	}

	@Test
	public void shouldSetRepositoriesWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<Repository> repositories = mock(SortedSet.class);
		project.setRepositories(repositories);
		verifyZeroInteractions(repositories);
	}

	@Test
	public void shouldAddRepositoryIfItDoesNotConflictWithExistingOnes() {
		project.addRepository(new Repository("name", RepositoryType.LOCAL_DIRECTORY, ""));
		project.addRepository(new Repository("other name", RepositoryType.LOCAL_DIRECTORY, ""));
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				project.addRepository(new Repository("name", RepositoryType.LOCAL_DIRECTORY, ""));
			}
		}).throwsException().withMessage("Repository named \"name\" already exists in the project.");
	}

	@Test
	public void shouldRemoveRepository() {
		Repository repository = mock(Repository.class);
		SortedSet<Repository> repositories = spy(sortedSet(repository));
		project.setRepositories(repositories);

		project.removeRepository(repository);
		assertTrue(project.getRepositories().isEmpty());
		verify(repository).setProject(null);
	}

	@Test
	public void shouldAssertSaved() {
		prepareSave(42L);

		project.assertSaved();
		verify(dao).save(project);

		project.assertSaved();
		verifyNoMoreInteractions(dao);
	}

	@Test
	public void shouldRequireNameToSave() {
		project.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				project.save();
			}
		}).throwsException().withMessage("Project requires name.");
	}

	@Test
	public void shouldUpdateIdAndSaveRepositoriesOnSave() {
		Long id = mock(Long.class);
		Repository repository = mock(Repository.class);
		prepareSave(id, repository);

		assertFalse(project.hasId());
		project.save();
		assertSame(id, project.getId());
		verify(repository).save();
	}

	private void prepareSave(Long id, Repository... repositories) {
		project.setRepositories(sortedSet(repositories));
		when(dao.save(project)).thenReturn(id);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		project.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfSaved() {
		Long id = mock(Long.class);
		Whitebox.setInternalState(project, "id", id);

		assertTrue(project.hasId());
		project.delete();
		assertFalse(project.hasId());
		verify(dao).delete(id);
	}

	@Test
	public void shouldNotifyRepositoriesOfDeletion() {
		Repository repository = mock(Repository.class);
		project.setRepositories(sortedSet(repository));

		project.delete();
		verify(repository).deleted();
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(project.getName(), "" + project);
	}
}