package org.kalibro.client;

import static org.kalibro.core.model.ProjectFixtures.*;
import static org.kalibro.core.model.enums.ProjectState.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.ProjectStateChangeSupport;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.dao.ProjectDaoFake;
import org.powermock.api.mockito.PowerMockito;

public class ProjectStateTrackerTest extends KalibroTestCase {

	private Project project;
	private ProjectDao projectDao;
	private ProjectStateListener listener;
	private ProjectStateChangeSupport changeSupport;

	private ProjectStateTracker tracker;

	@Before
	public void setUp() {
		saveProject();
		mockChangeSupport();
		tracker = new ProjectStateTracker(projectDao, changeSupport);
	}

	private void saveProject() {
		project = helloWorld();
		projectDao = new ProjectDaoFake();
		projectDao.save(project);
	}

	private void mockChangeSupport() {
		listener = PowerMockito.mock(ProjectStateListener.class);
		changeSupport = PowerMockito.spy(new ProjectStateChangeSupport());
		changeSupport.addProjectStateListener(project.getName(), listener);
		PowerMockito.doNothing().when(changeSupport).fireProjectStateChanged(anyString(), any(ProjectState.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyIfNothingHappened() {
		tracker.perform();
		assertNeverFired();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyIfProjectIsNotListen() {
		Project newProject = new Project();
		projectDao.save(newProject);
		tracker.perform();
		assertNeverFired();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyIfStateDidNotChange() {
		Project listenProject = newHelloWorld();
		listenProject.setLicense("Changing license, not state.");
		listenProject.setState(READY);
		projectDao.save(listenProject);
		tracker.perform();
		assertNeverFired();
	}

	private void assertNeverFired() {
		verify(changeSupport, never()).fireProjectStateChanged(anyString(), any(ProjectState.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyIfNewListenProjectIsSaved() {
		Project newProject = new Project();
		newProject.setName("New project");
		newProject.setState(LOADING);
		changeSupport.addProjectStateListener(newProject.getName(), listener);
		projectDao.save(newProject);
		tracker.perform();
		verify(changeSupport, times(1)).fireProjectStateChanged(newProject.getName(), LOADING);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyIfListenProjectIsRemoved() {
		projectDao.removeProject(project.getName());
		tracker.perform();
		verify(changeSupport, times(1)).fireProjectStateChanged(project.getName(), NEW);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyIfListenProjectStateChanges() {
		Project listenProject = newHelloWorld();
		listenProject.setState(LOADING);
		projectDao.save(listenProject);
		tracker.perform();
		verify(changeSupport, times(1)).fireProjectStateChanged(project.getName(), LOADING);
	}
}