package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.kalibro.core.model.enums.ProjectState.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.ProjectStateChangeSupport;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, ProjectTaskExecutor.class})
public class ProjectTaskExecutorTest extends KalibroTestCase {

	private Project project;
	private MyExecutor projectTaskExecutor;

	private Long time;
	private Task task;

	@Before
	public void setUp() {
		project = newHelloWorld();
		project.setState(NEW);
		projectTaskExecutor = new MyExecutor(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkExecution() throws Exception {
		ProjectState taskState = projectTaskExecutor.getTaskState();

		projectTaskExecutor = PowerMockito.spy(projectTaskExecutor);
		task = PowerMockito.mock(Task.class);

		PowerMockito.doNothing().when(projectTaskExecutor, "updateProjectState", taskState);

		projectTaskExecutor.execute();
		PowerMockito.verifyPrivate(projectTaskExecutor).invoke("updateProjectState", taskState);
		Mockito.verify(task).setListener(projectTaskExecutor);
		Mockito.verify(task).executeInBackground();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskDone() {
		projectTaskExecutor.taskFinished(mockReport(42, null));
		assertEquals(42L, time.longValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskHalted() {
		executeTestWithSaveMocked(new Runnable() {

			@Override
			public void run() {
				Exception error = new Exception();
				projectTaskExecutor.taskFinished(mockReport(42, error));
				assertEquals(42L, time.longValue());
				assertSame(error, project.getError());
			}
		});
	}

	private TaskReport mockReport(long executionTime, Exception error) {
		TaskReport report = PowerMockito.mock(TaskReport.class);
		PowerMockito.when(report.getExecutionTime()).thenReturn(executionTime);
		PowerMockito.when(report.isTaskDone()).thenReturn(error == null);
		PowerMockito.when(report.getError()).thenReturn(error);
		return report;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checlUpdateProjectState() {
		executeTestWithSaveMocked(new Runnable() {

			@Override
			public void run() {
				projectTaskExecutor.updateProjectState(READY);
				assertSame(READY, project.getState());
			}
		});
	}

	private void executeTestWithSaveMocked(Runnable test) {
		ProjectDao projectDao = PowerMockito.mock(ProjectDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.mockStatic(ProjectStateChangeSupport.class);
		PowerMockito.when(Kalibro.getProjectDao()).thenReturn(projectDao);

		test.run();
		Mockito.verify(projectDao).save(project);
		PowerMockito.verifyStatic();
		Kalibro.fireProjectStateChanged(project);
	}

	private class MyExecutor extends ProjectTaskExecutor {

		public MyExecutor(Project project) {
			super(new ProjectResult(project));
		}

		@Override
		protected ProjectState getTaskState() {
			return ANALYZING;
		}

		@Override
		protected Task getTask() {
			return task;
		}

		@Override
		protected void setTaskExecutionTime(long executionTime) {
			time = executionTime;
		}

		@Override
		protected void continueProcessing(TaskReport report) {
			return;
		}
	}
}