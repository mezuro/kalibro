package org.kalibro.core.persistence.database;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.analizo.AnalizoStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseToolDatabaseDao.class)
public class BaseToolDatabaseDaoTest extends KalibroTestCase {

	private BaseTool baseTool;
	private DatabaseManager databaseManager;

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		baseTool = new AnalizoStub().getBaseTool();
		databaseManager = PowerMockito.mock(DatabaseManager.class);
		dao = PowerMockito.spy(new BaseToolDatabaseDao(databaseManager));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSave() {
		dao.save(baseTool);

		ArgumentCaptor<BaseToolRecord> captor = ArgumentCaptor.forClass(BaseToolRecord.class);
		Mockito.verify(databaseManager).save(captor.capture());
		assertEquals(baseTool, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllBaseToolNames() {
		PowerMockito.doReturn(Arrays.asList("4", "2")).when(dao).getAllNames();
		assertDeepEquals(dao.getBaseToolNames(), "4", "2");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBaseToolByName() {
		PowerMockito.doReturn(baseTool).when(dao).getByName("42");
		assertSame(baseTool, dao.getBaseTool("42"));
	}
}