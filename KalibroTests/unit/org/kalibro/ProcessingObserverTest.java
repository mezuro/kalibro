package org.kalibro;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.SortedSet;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.processing.MailSender;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, MailSender.class})
public class ProcessingObserverTest extends UnitTest {

	private ProcessingObserver processingObserver;
	private Repository repository;
	private Email email;

	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "New repository";
	private static final String REPOSITORY_COMPLETE_NAME = "My project - " + REPOSITORY_NAME;

	private ProcessingObserverDao dao;

	@Before
	public void setUp() {
		processingObserver = new ProcessingObserver();
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
		dao = mock(ProcessingObserverDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProcessingObserverDao()).thenReturn(dao);
		when(dao.save(processingObserver, REPOSITORY_ID)).thenReturn(ID);
	}

	private void mockMailSender() {
		email = mock(Email.class);
		mockStatic(MailSender.class);
		when(MailSender.createEmptyEmailWithSender()).thenReturn(email);
	}

	@Test
	public void shouldGetAllProcessingObservers() {
		SortedSet<ProcessingObserver> processingObservers = mock(SortedSet.class);
		when(dao.all()).thenReturn(processingObservers);
		assertSame(processingObservers, ProcessingObserver.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}
	
	private ProcessingObserver withName(String name) {
		return new ProcessingObserver(name, "Any email");
	}

	@Test
	public void checkConstruction() {
		assertFalse(processingObserver.hasId());
		assertEquals("New name", processingObserver.getName());
		assertEquals("New email", processingObserver.getEmail());
	}

	@Test
	public void shouldNotSaveIfRepositoryIsNull() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(null);
			}
		}).throwsException().withMessage("ProcessingObserver is not related to any repository.");
	}

	@Test
	public void shouldRequireNameToSave() {
		processingObserver.setName(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(repository);
			}
		}).throwsException().withMessage("ProcessingObserver requires name.");
	}
	
	@Test
	public void shouldRequireEmailToSave() {
		processingObserver.setEmail(" ");
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				processingObserver.save(repository);
			}
		}).throwsException().withMessage("ProcessingObserver requires email.");
	}
	
	@Test
	public void shouldSaveIfRepositoryIsNotNull() {
		assertFalse(processingObserver.hasId());
		processingObserver.save(repository);
		assertEquals(processingObserver.getId(), ID);
	}

	@Test
	public void shouldIgnoreDeleteIfIsNotSaved() {
		processingObserver.delete();
		verify(dao, never()).delete(any(Long.class));
	}

	@Test
	public void shouldDeleteIfIsSaved() {
		Whitebox.setInternalState(processingObserver, "id", ID);

		assertTrue(processingObserver.hasId());
		processingObserver.delete();
		assertFalse(processingObserver.hasId());
		verify(dao).delete(ID);
	}


    @Test
    public void shouldSendEmailWithErrorMessage() {
        processingObserver.update(repository, ProcessState.ERROR);
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
		processingObserver.update(repository, ProcessState.READY);
		verify(email).setSubject(REPOSITORY_COMPLETE_NAME + " processing results");
		verify(email).setText("Processing results in repository " + REPOSITORY_NAME +
			" has finished successfully.\n\nThis is an automatic message." +
			" Please, do not reply.");
		verify(email).addRecipient("New name", "New email", RecipientType.TO);
		verifyStatic();
        MailSender.sendEmail(email);
	}
}
