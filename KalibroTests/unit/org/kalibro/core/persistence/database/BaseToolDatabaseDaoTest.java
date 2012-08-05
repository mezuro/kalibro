package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseToolDatabaseDao.class)
public class BaseToolDatabaseDaoTest extends KalibroTestCase {

	private BaseTool baseTool;
	private DatabaseManager databaseManager;

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		baseTool = analizoStub();
		databaseManager = mock(DatabaseManager.class);
		dao = spy(new BaseToolDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveKnownBaseTools() throws Exception {
		doNothing().when(dao, "save", any());
		dao.saveBaseTools();
		verifyPrivate(dao).invoke("save", AnalizoMetricCollector.class);
		verifyPrivate(dao).invoke("save", CheckstyleMetricCollector.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveBaseToolByClass() throws Exception {
		Whitebox.invokeMethod(dao, "save", AnalizoStub.class);

		ArgumentCaptor<BaseToolRecord> captor = ArgumentCaptor.forClass(BaseToolRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertDeepEquals(baseTool, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveIfCannotInstantiateBaseTool() throws Exception {
		Whitebox.invokeMethod(dao, "save", MetricCollector.class);
		Mockito.verify(databaseManager, Mockito.never()).save(any());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllBaseToolNames() {
		doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getBaseToolNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBaseToolByName() {
		doReturn(baseTool).when(dao).getByName("42");
		assertSame(baseTool, dao.getBaseTool("42"));
	}
}