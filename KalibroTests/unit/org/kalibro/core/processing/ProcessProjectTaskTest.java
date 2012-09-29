package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.ModuleResultFixtures.newHelloWorldResults;
import static org.kalibro.ProjectFixtures.*;

import java.util.Collection;
import java.util.Map;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dao.ProjectResultDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, ProcessProjectTask.class, KalibroSettings.class})
public class ProcessProjectTaskTest extends UnitTest {

	private Project project;
	private ProjectResult projectResult;
	private Collection<ModuleResult> moduleResults;

	private ProjectDao projectDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private ProjectResultDao projectResultDao;

	private LoadSourceTask loadTask;
	private CollectMetricsTask collectTask;
	private AnalyzeResultsTask analyzeTask;

	private ProcessProjectTask processTask;
	private Mailer mailerMock;

	@Before
	public void setUp() throws Exception {
		project = newHelloWorld();
		project.setState(ProjectState.NEW);
		moduleResults = newHelloWorldResults();
		mockKalibro();
		mockSubtasks();
		mockMailer();
		processTask = new ProcessProjectTask(PROJECT_NAME);
	}

	private void mockMailer() {
		KalibroSettings kalibroSettingsMock = mock(KalibroSettings.class);
		MailSettings mailSettingsMock = mock(MailSettings.class);
		mailerMock = mock(Mailer.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettingsMock);
		when(kalibroSettingsMock.getMailSettings()).thenReturn(mailSettingsMock);
		when(mailSettingsMock.createMailer()).thenReturn(mailerMock);
	}

	private void mockKalibro() {
		projectDao = mock(ProjectDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		projectResultDao = mock(ProjectResultDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(projectDao);
		when(DaoFactory.getModuleResultDao()).thenReturn(moduleResultDao);
		when(DaoFactory.getProjectResultDao()).thenReturn(projectResultDao);
		when(projectDao.getProject(PROJECT_NAME)).thenReturn(project);
	}

	private void mockSubtasks() throws Exception {
		loadTask = mock(LoadSourceTask.class);
		collectTask = mock(CollectMetricsTask.class);
		analyzeTask = mock(AnalyzeResultsTask.class);
		projectResult = mock(ProjectResult.class);
		Map<Module, ModuleResult> resultMap = mock(Map.class);
		whenNew(LoadSourceTask.class).withArguments(project).thenReturn(loadTask);
		when(loadTask.executeSubTask()).thenReturn(projectResult);
		whenNew(CollectMetricsTask.class).withArguments(projectResult).thenReturn(collectTask);
		when(collectTask.executeSubTask()).thenReturn(resultMap);
		whenNew(AnalyzeResultsTask.class).withArguments(projectResult, resultMap).thenReturn(analyzeTask);
		when(analyzeTask.executeSubTask()).thenReturn(moduleResults);
	}

	@Test
	public void shouldExecuteSubtasks() {
		processTask.perform();

		InOrder order = Mockito.inOrder(loadTask, collectTask, analyzeTask);
		order.verify(loadTask).executeSubTask();
		order.verify(collectTask).executeSubTask();
		order.verify(analyzeTask).executeSubTask();
	}

	@Test
	public void shouldSaveProjectWithUpdatedState() {
		processTask.perform();
		assertEquals(ProjectState.READY, project.getState());
		Mockito.verify(projectDao).save(project);
	}

	@Test
	public void shouldSaveResults() {
		processTask.perform();

		InOrder order = Mockito.inOrder(projectResultDao, moduleResultDao);
		order.verify(projectResultDao).save(projectResult);
		for (ModuleResult moduleResult : moduleResults)
			order.verify(moduleResultDao).save(moduleResult, projectResult);
	}

	@Test
	public void shouldSaveProjectWithError() {
		RuntimeException error = mock(RuntimeException.class);
		when(loadTask.executeSubTask()).thenThrow(error);

		processTask.perform();
		assertEquals(ProjectState.ERROR, project.getState());
		assertSame(error, project.getError());
		Mockito.verify(projectDao).save(project);
	}

	@Test
	public void shouldSendMailAfterProcess() throws Exception {
		Email email = mock(Email.class);
		whenNew(Email.class).withNoArguments().thenReturn(email);
		processTask.perform();

		verify(mailerMock).sendMail(email);
		verify(email).addRecipient("aaa@example.com", "aaa@example.com", RecipientType.TO);
		verify(email).addRecipient("bbb@example.com", "bbb@example.com", RecipientType.TO);
	}
}
