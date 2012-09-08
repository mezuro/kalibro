package org.kalibro.core.persistence;

import static org.junit.Assert.assertSame;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.record.BaseToolRecord;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseToolDatabaseDao.class)
public class BaseToolDatabaseDaoTest extends TestCase {

	private static final List<String> BASE_TOOL_NAMES = Arrays.asList("Analizo", "Checkstyle");

	private BaseTool baseTool;
	private RecordManager recordManager;

	private BaseToolDatabaseDao dao;

	@Before
	public void setUp() {
		baseTool = analizoStub();
		recordManager = mock(RecordManager.class);
		dao = spy(new BaseToolDatabaseDao(recordManager));
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
		doReturn(new ArrayList<String>()).when(dao).getAllNames();
		Whitebox.invokeMethod(dao, "save", AnalizoStub.class);

		ArgumentCaptor<BaseToolRecord> captor = ArgumentCaptor.forClass(BaseToolRecord.class);
		Mockito.verify(recordManager).save(captor.capture());
		assertDeepEquals(baseTool, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveIfCannotInstantiateBaseTool() throws Exception {
		doReturn(new ArrayList<String>()).when(dao).getAllNames();
		Whitebox.invokeMethod(dao, "save", MetricCollector.class);
		Mockito.verify(recordManager, never()).save(any());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveIfBaseToolAlreadyExists() throws Exception {
		doReturn(BASE_TOOL_NAMES).when(dao).getAllNames();
		Whitebox.invokeMethod(dao, "save", AnalizoStub.class);
		Mockito.verify(recordManager, never()).save(any());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListAllBaseToolNames() {
		doReturn(BASE_TOOL_NAMES).when(dao).getAllNames();
		assertDeepEquals(BASE_TOOL_NAMES, dao.getBaseToolNames());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetBaseToolByName() {
		doReturn(baseTool).when(dao).getByName("Analizo");
		assertSame(baseTool, dao.getBaseTool("Analizo"));
	}
}