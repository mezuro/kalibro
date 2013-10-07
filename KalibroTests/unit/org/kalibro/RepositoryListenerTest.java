package org.kalibro;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.SortedSet;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.processing.MailSender;
import org.kalibro.core.processing.ProcessTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryListenerDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, MailSender.class})
public class RepositoryListenerTest extends UnitTest {

	private RepositoryListener repositoryListener;
	private Repository repository;
	private Email email;

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "New repository";
	private static final String REPOSITORY_COMPLETE_NAME = "My project - " + REPOSITORY_NAME;

	private RepositoryListenerDao dao;

	@Before
	public void setUp() {
		repositoryListener = new RepositoryListener();
		mockRepository();
		mockDao();
		mockMailSender();
	}

	private void mockRepository() {
		repository = mock(Repository.class);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
		when(repository.getCompleteName()).thenReturn(REPOSITORY_COMPLETE_NAME);
	}

	private void mockDao() {
		dao = mock(RepositoryListenerDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryListenerDao()).thenReturn(dao);
		when(dao.save(repositoryListener, REPOSITORY_ID)).thenReturn(ID);
	}

	private void mockMailSender() {
		email = mock(Email.class);
		mockStatic(MailSender.class);
		when(MailSender.createEmptyEmailWithSender()).thenReturn(email);
	}

	@Test
	public void shouldGetAllRepositoryListeners() {
		SortedSet<RepositoryListener> repositoryListeners = mock(SortedSet.class);
		when(dao.all()).thenReturn(repositoryListeners);
		assertSame(repositoryListeners, RepositoryListener.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	private RepositoryListener withName(String name) {
		return new RepositoryListener(name, "Any email");
	}

	@Test
	public void checkConstruction() {
		assertFalse(repositoryListener.hasId());
		assertEquals("New name", repositoryListener.getName());
		assertEquals("New email", repositoryListener.getEmail());
	}

	@Test
	public void shouldNotSaveIfRepositoryIsNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(null);
			}
		}).throwsException().withMessage("Listener is not related to any repository.");
	}

	@Test
	public void shouldRequireNameToSave() {
		repositoryListener.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(repository);
			}
		}).throwsException().withMessage("RepositoryListener requires name.");
	}

	@Test
	public void shouldRequireEmailToSave() {
		repositoryListener.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryListener.save(repository);
			}
		}).throwsException().withMessage("RepositoryListener requires email.");
	}

	@Test
	public void shouldSaveIfRepositoryIsNotNull() {
		assertFalse(repositoryListener.hasId());
		repositoryListener.save(repository);
		assertEquals(repositoryListener.getId(), ID);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		repositoryListener.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfIsSaved() {
		Whitebox.setInternalState(repositoryListener, "id", ID);

		assertTrue(repositoryListener.hasId());
		repositoryListener.delete();
		assertFalse(repositoryListener.hasId());
		verify(dao).delete(ID);
	}

	@Test
	public void shouldSendEmailWithErrorMessage() {
		repositoryListener.taskFinished(mockReport(ProcessState.ERROR));
		verify(email).setSubject(REPOSITORY_COMPLETE_NAME + " processing results");
		verify(email).setText("Processing results in repository " + REPOSITORY_NAME +
			" has resulted in error.\n\nThis is an automatic message." +
			" Please, do not reply.");
		verify(email).addRecipient("New name", "New email", RecipientType.TO);
		verifyStatic();
		MailSender.sendEmail(email);
	}

	@Test
	public void shouldSendEmailWithSucessfulMessage() {
		repositoryListener.taskFinished(mockReport(ProcessState.READY));
		verify(email).setSubject(REPOSITORY_COMPLETE_NAME + " processing results");
		verify(email).setText("Processing results in repository " + REPOSITORY_NAME +
			" has finished successfully.\n\nThis is an automatic message." +
			" Please, do not reply.");
		verify(email).addRecipient("New name", "New email", RecipientType.TO);
		verifyStatic();
		MailSender.sendEmail(email);
	}

	private TaskReport<Void> mockReport(ProcessState processState) {
		TaskReport<Void> report = mock(TaskReport.class);
		ProcessTask task = mock(ProcessTask.class);
		when(report.getTask()).thenReturn(task);
		when(task.getRepository()).thenReturn(repository);
		when(task.getProcessState()).thenReturn(processState);
		return report;
	}
}
