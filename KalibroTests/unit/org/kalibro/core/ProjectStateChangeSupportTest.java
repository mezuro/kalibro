package org.kalibro.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.ProjectState;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProjectStateChangeSupport.class)
public class ProjectStateChangeSupportTest extends KalibroTestCase {

	private String projectName;
	private ProjectState newProjectState;
	private ProjectStateListener listener;
	private ProjectStateChangeFirer firer;

	private ProjectStateChangeSupport changeSupport;

	@Before
	public void setUp() throws Exception {
		projectName = "My project";
		newProjectState = ProjectState.READY;
		listener = PowerMockito.mock(ProjectStateListener.class);
		firer = PowerMockito.mock(ProjectStateChangeFirer.class);
		PowerMockito.whenNew(ProjectStateChangeFirer.class)
			.withArguments(projectName, newProjectState, listener).thenReturn(firer);

		changeSupport = new ProjectStateChangeSupport();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void listenProjectNamesShouldBeEmptyByDefault() {
		assertTrue(changeSupport.getListenProjectNames().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void addedProjectNamesShouldAppearOnListenProjects() {
		changeSupport.addProjectStateListener(projectName, listener);
		assertDeepEquals(changeSupport.getListenProjectNames(), projectName);

		String otherProjectName = "Other project name";
		changeSupport.addProjectStateListener(otherProjectName, listener);
		assertDeepEquals(changeSupport.getListenProjectNames(), projectName, otherProjectName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void removedProjectsShouldNotAppearOnListenProjects() {
		changeSupport.addProjectStateListener(projectName, listener);
		changeSupport.addProjectStateListener("Other project name", listener);

		changeSupport.removeProjectStateListener(listener);
		assertTrue(changeSupport.getListenProjectNames().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyListenerIfNotRegistered() {
		assertNotNotified();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyListenerOfDifferentProject() {
		changeSupport.addProjectStateListener("Different project name", listener);
		assertNotNotified();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyRemovedListener() {
		changeSupport.addProjectStateListener(projectName, listener);
		changeSupport.removeProjectStateListener(listener);
		assertNotNotified();
	}

	private void assertNotNotified() {
		changeSupport.fireProjectStateChanged(projectName, newProjectState);
		PowerMockito.verifyNew(ProjectStateChangeFirer.class, never());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyProjectListener() {
		changeSupport.addProjectStateListener(projectName, listener);
		changeSupport.fireProjectStateChanged(projectName, newProjectState);
		verify(firer).executeInBackground();
	}
}