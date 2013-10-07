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
import org.kalibro.dao.RepositoryObserverDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, MailSender.class})
public class RepositoryObserverTest extends UnitTest {

	private RepositoryObserver repositoryObserver;
	private Repository repository;
	private Email email;

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "New repository";
	private static final String REPOSITORY_COMPLETE_NAME = "My project - " + REPOSITORY_NAME;

	private RepositoryObserverDao dao;

	@Before
	public void setUp() {
		repositoryObserver = new RepositoryObserver();
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
		dao = mock(RepositoryObserverDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryObserverDao()).thenReturn(dao);
		when(dao.save(repositoryObserver, REPOSITORY_ID)).thenReturn(ID);
	}

	private void mockMailSender() {
		email = mock(Email.class);
		mockStatic(MailSender.class);
		when(MailSender.createEmptyEmailWithSender()).thenReturn(email);
	}

	@Test
	public void shouldGetAllRepositoryObservers() {
		SortedSet<RepositoryObserver> repositoryObservers = mock(SortedSet.class);
		when(dao.all()).thenReturn(repositoryObservers);
		assertSame(repositoryObservers, RepositoryObserver.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	private RepositoryObserver withName(String name) {
		return new RepositoryObserver(name, "Any email");
	}

	@Test
	public void checkConstruction() {
		assertFalse(repositoryObserver.hasId());
		assertEquals("New name", repositoryObserver.getName());
		assertEquals("New email", repositoryObserver.getEmail());
	}

	@Test
	public void shouldNotSaveIfRepositoryIsNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(null);
			}
		}).throwsException().withMessage("RepositoryObserver is not related to any repository.");
	}

	@Test
	public void shouldRequireNameToSave() {
		repositoryObserver.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(repository);
			}
		}).throwsException().withMessage("RepositoryObserver requires name.");
	}

	@Test
	public void shouldRequireEmailToSave() {
		repositoryObserver.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositoryObserver.save(repository);
			}
		}).throwsException().withMessage("RepositoryObserver requires email.");
	}

	@Test
	public void shouldSaveIfRepositoryIsNotNull() {
		assertFalse(repositoryObserver.hasId());
		repositoryObserver.save(repository);
		assertEquals(repositoryObserver.getId(), ID);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		repositoryObserver.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfIsSaved() {
		Whitebox.setInternalState(repositoryObserver, "id", ID);

		assertTrue(repositoryObserver.hasId());
		repositoryObserver.delete();
		assertFalse(repositoryObserver.hasId());
		verify(dao).delete(ID);
	}

	@Test
	public void shouldSendEmailWithErrorMessage() {
		repositoryObserver.taskFinished(mockReport(ProcessState.ERROR));
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
		repositoryObserver.taskFinished(mockReport(ProcessState.READY));
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
