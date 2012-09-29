package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.BaseToolFixtures.analizoStub;

import javax.swing.JList;
import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.NativeMetric;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.list.ListListener;
import org.kalibro.desktop.swingextension.list.Table;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(DaoFactory.class)
public class ChooseNativeMetricPanelTest extends UnitTest {

	private BaseTool analizo;

	private ChooseNativeMetricPanel panel;
	private Table<NativeMetric> table;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		analizo = analizoStub();
		mockDaoFactory();
		panel = new ChooseNativeMetricPanel();
		finder = new ComponentFinder(panel);
		spyMetricTable();
	}

	private void mockDaoFactory() {
		BaseToolDao dao = PowerMockito.mock(BaseToolDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getBaseToolDao()).thenReturn(dao);
		PowerMockito.when(dao.all()).thenReturn(asSortedSet(analizo));
	}

	private void spyMetricTable() {
		table = PowerMockito.spy(finder.find("supportedMetrics", Table.class));
		Whitebox.setInternalState(panel, Table.class, table);
	}

	@Test
	public void shouldShowBaseToolNames() {
		JList baseToolList = baseToolList();
		assertEquals(1, baseToolList.getModel().getSize());
		assertEquals("Analizo", baseToolList.getModel().getElementAt(0));
	}

	@Test
	public void shouldShowNativeMetricsFromBaseTool() {
		assertTrue(table.getData().isEmpty());

		baseToolList().setSelectedIndex(0);
		assertDeepEquals(table.getData(), analizo.getSupportedMetrics());
	}

	@Test
	public void shouldShowBaseToolDescription() {
		analizo.setDescription("This is the description of Analizo");
		baseToolList().setSelectedIndex(0);
		assertTrue(descriptionPane().get().contains(analizo.getDescription()));
	}

	@Test
	public void shouldShowMetricDescription() {
		baseToolList().setSelectedIndex(0);
		NativeMetric firstMetric = table.getData().get(0);
		firstMetric.setDescription("This is the description of the first metric");

		selectFirsMetric();
		assertTrue(descriptionPane().get().contains(firstMetric.getDescription()));
	}

	@Test
	public void shouldConfirmMetricSelection() {
		baseToolList().setSelectedIndex(0);
		assertFalse(panel.hasSelectedMetric());

		selectFirsMetric();
		assertTrue(panel.hasSelectedMetric());
	}

	@Test
	public void shouldRetrieveSelectedMetric() {
		baseToolList().setSelectedIndex(0);
		selectFirsMetric();
		NativeMetric firstMetric = table.getData().get(0);
		assertSame(firstMetric, panel.get());
	}

	@Test
	public void shouldAddListListener() {
		ListListener<NativeMetric> listener = PowerMockito.mock(ListListener.class);
		panel.addListListener(listener);
		Mockito.verify(table).addListListener(listener);
	}

	private JList baseToolList() {
		return finder.find("baseTools", JList.class);
	}

	private TextField descriptionPane() {
		return finder.find("description", TextField.class);
	}

	private void selectFirsMetric() {
		finder.find("supportedMetrics", JTable.class).getSelectionModel().setSelectionInterval(0, 0);
	}
}