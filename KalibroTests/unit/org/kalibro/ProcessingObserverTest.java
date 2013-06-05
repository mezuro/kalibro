package org.kalibro;

import static org.junit.Assert.*;

import java.util.Random;

import org.codemonkey.simplejavamail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.processing.MailSender;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingObserverDao;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProcessingObserverTest extends UnitTest {

	
	private ProcessingObserver processingObserver;
	private Repository repository;
	private MailSender mailSender;
	
	private static final Long ID = new Random().nextLong();
	private static final Long REPOSITORY_ID = new Random().nextLong();
	
	private ProcessingObserverDao dao;
	
	@Before
	public void setUp() {
		processingObserver = new ProcessingObserver();
		mockRepository();
		mockDao();
	}

	private void mockRepository() {
		repository = mock(Repository.class);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
	}

	private void mockDao() {
		dao = mock(ProcessingObserverDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProcessingObserverDao()).thenReturn(dao);
		when(dao.save(processingObserver, REPOSITORY_ID)).thenReturn(ID);
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
		}).throwsException().withMessage("Notification is not related to any repository.");
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
	
}
