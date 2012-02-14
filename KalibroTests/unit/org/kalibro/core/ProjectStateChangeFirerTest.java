package org.kalibro.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.ProjectState;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProjectStateChangeFirer.class)
public class ProjectStateChangeFirerTest extends KalibroTestCase {

	private String projectName;
	private ProjectState newProjectState;
	private ProjectStateListener listener;
	private ProjectStateChangeFirer firer;

	@Before
	public void setUp() {
		projectName = "My project";
		newProjectState = ProjectState.READY;
		listener = PowerMockito.mock(ProjectStateListener.class);
		firer = new ProjectStateChangeFirer(projectName, newProjectState, listener);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyListenerOnRun() {
		firer.run();
		Mockito.verify(listener).projectStateChanged(projectName, newProjectState);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRunOnFire() throws Exception {
		Thread thread = PowerMockito.mock(Thread.class);
		PowerMockito.whenNew(Thread.class).withParameterTypes(Runnable.class)
			.withArguments(firer).thenReturn(thread);

		firer.fire();
		Mockito.verify(thread).start();
	}
}