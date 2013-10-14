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
import org.kalibro.dao.RepositorySubscriberDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, MailSender.class})
public class RepositorySubscriberTest extends UnitTest {

	private RepositorySubscriber repositorySubscriber;
	private Repository repository;
	private Email email;

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "New repository";
	private static final String REPOSITORY_COMPLETE_NAME = "My project - " + REPOSITORY_NAME;

	private RepositorySubscriberDao dao;

	@Before
	public void setUp() {
		repositorySubscriber = new RepositorySubscriber();
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
		dao = mock(RepositorySubscriberDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositorySubscriberDao()).thenReturn(dao);
		when(dao.save(repositorySubscriber, REPOSITORY_ID)).thenReturn(ID);
	}

	private void mockMailSender() {
		email = mock(Email.class);
		mockStatic(MailSender.class);
		when(MailSender.createEmptyEmailWithSender()).thenReturn(email);
	}

	@Test
	public void shouldGetAllRepositorySubscribers() {
		SortedSet<RepositorySubscriber> repositorySubscribers = mock(SortedSet.class);
		when(dao.all()).thenReturn(repositorySubscribers);
		assertSame(repositorySubscribers, RepositorySubscriber.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	private RepositorySubscriber withName(String name) {
		return new RepositorySubscriber(name, "Any email");
	}

	@Test
	public void checkConstruction() {
		assertFalse(repositorySubscriber.hasId());
		assertEquals("New name", repositorySubscriber.getName());
		assertEquals("New email", repositorySubscriber.getEmail());
	}

	@Test
	public void shouldNotSaveIfRepositoryIsNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(null);
			}
		}).throwsException().withMessage("Repository subscriber is not related to any repository.");
	}

	@Test
	public void shouldRequireNameToSave() {
		repositorySubscriber.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(repository);
			}
		}).throwsException().withMessage("Repository subscriber requires name.");
	}

	@Test
	public void shouldRequireEmailToSave() {
		repositorySubscriber.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				repositorySubscriber.save(repository);
			}
		}).throwsException().withMessage("Repository subscriber requires email.");
	}

	@Test
	public void shouldSaveIfRepositoryIsNotNull() {
		assertFalse(repositorySubscriber.hasId());
		repositorySubscriber.save(repository);
		assertEquals(repositorySubscriber.getId(), ID);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		repositorySubscriber.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfIsSaved() {
		Whitebox.setInternalState(repositorySubscriber, "id", ID);

		assertTrue(repositorySubscriber.hasId());
		repositorySubscriber.delete();
		assertFalse(repositorySubscriber.hasId());
		verify(dao).delete(ID);
	}

	@Test
	public void shouldSendEmailWithErrorMessage() {
		repositorySubscriber.taskFinished(mockReport(ProcessState.ERROR));
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
		repositorySubscriber.taskFinished(mockReport(ProcessState.READY));
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