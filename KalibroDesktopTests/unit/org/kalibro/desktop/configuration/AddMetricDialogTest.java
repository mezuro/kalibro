package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;
import static org.mockito.Matchers.any;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.RadioButton;
import org.kalibro.desktop.swingextension.list.Table;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(DaoFactory.class)
public class AddMetricDialogTest extends TestCase {

	private AddMetricDialog dialog;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		mockDaoFactory();
		dialog = new AddMetricDialog();
		finder = new ComponentFinder(dialog);
	}

	private void mockDaoFactory() {
		BaseToolDao dao = PowerMockito.mock(BaseToolDao.class);
		PowerMockito.mockStatic(DaoFactory.class);
		PowerMockito.when(DaoFactory.getBaseToolDao()).thenReturn(dao);
		PowerMockito.when(dao.getBaseToolNames()).thenReturn(Arrays.asList("Analizo"));
		PowerMockito.when(dao.getBaseTool("Analizo")).thenReturn(analizoStub());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void compoundShouldBeSelectedByDefault() {
		assertTrue(radio("compound").isSelected());
		assertFalse(radio("native").isSelected());
		assertFalse(nativeMetricPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSwitchToNative() {
		radio("native").doClick();
		assertFalse(radio("compound").isSelected());
		assertTrue(radio("native").isSelected());
		assertTrue(nativeMetricPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSwitchBackToCompound() {
		radio("native").doClick();
		radio("compound").doClick();
		assertTrue(radio("compound").isSelected());
		assertFalse(radio("native").isSelected());
		assertFalse(nativeMetricPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void okButtonShouldBeEnabledOnlyWhenMetricIsSelected() {
		Button okButton = button("ok");
		assertTrue(okButton.isEnabled());

		radio("native").doClick();
		assertFalse(okButton.isEnabled());

		selectFirstBaseTool();
		assertFalse(okButton.isEnabled());

		selectFirstMetric();
		assertTrue(okButton.isEnabled());

		metricSelectionModel().clearSelection();
		assertFalse(okButton.isEnabled());

		radio("compound").doClick();
		assertTrue(okButton.isEnabled());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void cancelShouldCloseDialog() {
		assertTrue(dialog.isDisplayable());
		button("cancel").doClick();
		assertFalse(dialog.isDisplayable());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddOkListener() {
		ActionListener listener = PowerMockito.mock(ActionListener.class);
		dialog.addOkListener(listener);

		button("ok").doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveNewCompoundMetricWhenSelected() {
		assertDeepEquals(new CompoundMetric(), dialog.getMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveNativeMetricWhenSelected() {
		radio("native").doClick();
		selectFirstBaseTool();
		selectFirstMetric();
		assertSame(firstMetric(), dialog.getMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void doubleClickMetricShouldMeanOkClick() {
		ActionListener listener = PowerMockito.mock(ActionListener.class);
		dialog.addOkListener(listener);

		radio("native").doClick();
		selectFirstBaseTool();
		doubleClickFirstMetric();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}

	private RadioButton radio(String name) {
		return finder.find(name, RadioButton.class);
	}

	private ChooseNativeMetricPanel nativeMetricPanel() {
		return finder.find("chooseNativeMetric", ChooseNativeMetricPanel.class);
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}

	private void selectFirstBaseTool() {
		finder.find("baseTools", JList.class).setSelectedIndex(0);
	}

	private void selectFirstMetric() {
		metricSelectionModel().setSelectionInterval(0, 0);
	}

	private void doubleClickFirstMetric() {
		JTable table = metricTable();
		metricSelectionModel().setSelectionInterval(0, 0);
		for (MouseListener mouseListener : table.getMouseListeners())
			mouseListener.mouseClicked(new MouseEvent(table, 0, 0, 0, 0, 0, 0, 0, 2, false, 0));
	}

	private ListSelectionModel metricSelectionModel() {
		return metricTable().getSelectionModel();
	}

	private NativeMetric firstMetric() {
		return (NativeMetric) finder.find("supportedMetrics", Table.class).getData().get(0);
	}

	private JTable metricTable() {
		return finder.find("supportedMetrics", JTable.class);
	}
}