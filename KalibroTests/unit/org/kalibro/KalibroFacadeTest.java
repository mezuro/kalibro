package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.ProjectStateChangeSupport;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KalibroFacade.class)
public class KalibroFacadeTest extends KalibroTestCase {

	private DaoFactory factory;
	private Project project;
	private ProjectStateListener listener;
	private ProjectStateChangeSupport changeSupport;

	private KalibroFacade facade;

	@Before
	public void setUp() throws Exception {
		factory = PowerMockito.mock(DaoFactory.class);
		project = ProjectFixtures.helloWorld();
		listener = PowerMockito.mock(ProjectStateListener.class);
		changeSupport = PowerMockito.mock(ProjectStateChangeSupport.class);
		PowerMockito.whenNew(ProjectStateChangeSupport.class).withNoArguments().thenReturn(changeSupport);
		facade = new FacadeStub(factory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateDaoFactory() {
		assertSame(factory, facade.getDaoFactory());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddProjectStateListener() {
		facade.addProjectStateListener(project, listener);
		Mockito.verify(changeSupport).addProjectStateListener(project.getName(), listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveProjectStateListener() {
		facade.removeProjectStateListener(listener);
		Mockito.verify(changeSupport).removeProjectStateListener(listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFireProjectStateChanged() {
		facade.fireProjectStateChanged(project);
		Mockito.verify(changeSupport).fireProjectStateChanged(project.getName(), project.getState());
	}
}