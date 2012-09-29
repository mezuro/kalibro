package org.kalibro.core.persistence;

import static org.kalibro.BaseToolFixtures.analizoStub;

import java.util.ArrayList;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.MetricCollector;
import org.kalibro.core.persistence.record.BaseToolRecord;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseToolDatabaseDao.class)
public class BaseToolDatabaseDaoTest extends UnitTest {

	private BaseTool baseTool;
	private RecordManager recordManager;

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		baseTool = analizoStub();
		recordManager = mock(RecordManager.class);
		dao = spy(new BaseToolDatabaseDao(recordManager));
	}

	@Test
	public void shouldSaveKnownBaseTools() throws Exception {
		doReturn(null).when(dao, "save", any(MetricCollector.class));
		dao.saveBaseTools();
		verifyPrivate(dao).invoke("save", AnalizoMetricCollector.class);
		verifyPrivate(dao).invoke("save", CheckstyleMetricCollector.class);
	}

	@Test
	public void shouldSaveBaseToolByClass() throws Exception {
		doReturn(new ArrayList<BaseTool>()).when(dao).all();
		Whitebox.invokeMethod(dao, "save", AnalizoStub.class);

		ArgumentCaptor<BaseToolRecord> captor = ArgumentCaptor.forClass(BaseToolRecord.class);
		Mockito.verify(recordManager).save(captor.capture());
		assertDeepEquals(baseTool, captor.getValue().convert());
	}

	@Test
	public void shouldNotSaveIfCannotInstantiateBaseTool() throws Exception {
		doReturn(new ArrayList<BaseTool>()).when(dao).all();
		Whitebox.invokeMethod(dao, "save", MetricCollector.class);
		Mockito.verify(recordManager, never()).save(any());
	}

	@Test
	public void shouldNotSaveIfBaseToolAlreadyExists() throws Exception {
		doReturn(asSortedSet(analizoStub())).when(dao).all();
		Whitebox.invokeMethod(dao, "save", AnalizoStub.class);
		Mockito.verify(recordManager, never()).save(any());
	}
}