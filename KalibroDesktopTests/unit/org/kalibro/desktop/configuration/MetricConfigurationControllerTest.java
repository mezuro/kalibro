package org.kalibro.desktop.configuration;

import java.awt.Container;
import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MetricConfigurationController.class)
public class MetricConfigurationControllerTest extends UnitTest {

	private MetricConfiguration metricConfiguration;
	private Configuration configuration;
	private CardStackPanel cardStack;

	private AddMetricDialog addDialog;
	private MetricConfigurationPanel metricPanel;
	private ConfirmPanel<MetricConfiguration> confirmPanel;

	private MetricConfigurationController controller;

	@Before
	public void setUp() throws Exception {
		metricConfiguration = PowerMockito.mock(MetricConfiguration.class);
		configuration = PowerMockito.mock(Configuration.class);
		cardStack = PowerMockito.mock(CardStackPanel.class);

		addDialog = PowerMockito.mock(AddMetricDialog.class);
		metricPanel = PowerMockito.mock(MetricConfigurationPanel.class);
		confirmPanel = PowerMockito.mock(ConfirmPanel.class);

		setUpMocks();
		controller = new MetricConfigurationController(configuration, cardStack);
	}

	private void setUpMocks() throws Exception {
		PowerMockito.whenNew(MetricConfigurationPanel.class).withNoArguments().thenReturn(metricPanel);
		PowerMockito.whenNew(ConfirmPanel.class).withArguments(metricPanel).thenReturn(confirmPanel);
		PowerMockito.whenNew(AddMetricDialog.class).withNoArguments().thenReturn(addDialog);
	}

	@Test
	public void shouldListenToMetricConfigurationPanel() {
		verify(metricPanel).addRangesListener(controller);
	}

	@Test
	public void shouldListenToConfirmPanel() {
		verify(confirmPanel).addOkListener(controller);
		verify(confirmPanel).addCancelListener(controller);
	}

	@Test
	public void shouldShowAddMetricDialogOnAdd() {
		controller.addMetricConfiguration();
		verify(addDialog).addOkListener(controller);
		verify(addDialog).setVisible(true);
	}

	@Test
	public void shouldNotCloseAddDialogOnConflictingMetric() throws Exception {
		Exception error = new KalibroException("MetricConfigurationControllerTest");
		ErrorDialog errorDialog = mockErrorDialog();
		PowerMockito.when(addDialog.getMetric()).thenReturn(new CompoundMetric());
		PowerMockito.doThrow(error).when(configuration).addMetricConfiguration(any(MetricConfiguration.class));

		controller.addMetricConfiguration();
		clickButton(addDialog, "ok");

		verify(errorDialog).show(error);
		verify(cardStack, never()).push(confirmPanel);
		verify(addDialog, never()).dispose();
	}

	@Test
	public void shouldShowNewConfigurationForChosenMetricWhenAdding() throws Exception {
		Metric chosenMetric = PowerMockito.mock(Metric.class);
		PowerMockito.when(addDialog.getMetric()).thenReturn(chosenMetric);
		PowerMockito.whenNew(MetricConfiguration.class).withArguments(chosenMetric).thenReturn(metricConfiguration);

		controller.addMetricConfiguration();
		clickButton(addDialog, "ok");

		verify(configuration).addMetricConfiguration(metricConfiguration);
		verify(confirmPanel).set(metricConfiguration);
		verify(cardStack).push(confirmPanel);
		verify(addDialog).dispose();
	}

	@Test
	public void shouldShowMetricConfigurationWhenEditing() {
		controller.edit(metricConfiguration);
		verify(confirmPanel).set(metricConfiguration);
		verify(cardStack).push(confirmPanel);
	}

	@Test
	public void shouldJustPopPanelOnCancel() {
		clickButton(confirmPanel, "cancel");
		verify(cardStack).pop();
	}

	@Test
	public void shouldReplaceMetricConfigurationAndPopPanelOnConfirm() {
		Metric metric = PowerMockito.mock(Metric.class);
		MetricConfiguration oldConfiguration = PowerMockito.mock(MetricConfiguration.class);
		MetricConfiguration newConfiguration = PowerMockito.mock(MetricConfiguration.class);
		PowerMockito.when(oldConfiguration.getMetric()).thenReturn(metric);
		PowerMockito.when(metric.getName()).thenReturn("42");
		PowerMockito.when(confirmPanel.get()).thenReturn(newConfiguration);

		controller.edit(oldConfiguration);
		clickButton(confirmPanel, "ok");

		verify(configuration).replaceMetricConfiguration("42", newConfiguration);
		verify(cardStack).pop();
	}

	@Test
	public void shouldNotPopPanelOnError() throws Exception {
		Metric metric = PowerMockito.mock(Metric.class);
		PowerMockito.when(metricConfiguration.getMetric()).thenReturn(metric);
		PowerMockito.when(metric.getName()).thenReturn("42");

		Exception error = new KalibroException("MetricConfigurationControllerTest");
		ErrorDialog errorDialog = mockErrorDialog();
		PowerMockito.doThrow(error).when(configuration).replaceMetricConfiguration("42", null);

		controller.edit(metricConfiguration);
		clickButton(confirmPanel, "ok");

		verify(errorDialog).show(error);
		verify(cardStack, never()).pop();
	}

	private void clickButton(Container ancestor, String buttonName) {
		Button button = PowerMockito.mock(Button.class);
		ActionEvent event = PowerMockito.mock(ActionEvent.class);
		PowerMockito.when(event.getSource()).thenReturn(button);
		PowerMockito.when(button.getName()).thenReturn(buttonName);
		PowerMockito.when(ancestor.isAncestorOf(button)).thenReturn(true);
		controller.actionPerformed(event);
	}

	private ErrorDialog mockErrorDialog() throws Exception {
		ErrorDialog errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(any()).thenReturn(errorDialog);
		return errorDialog;
	}

	@Test
	public void shouldAddRange() throws Exception {
		RangeController rangeController = mockRangeController();
		controller.add();
		verify(rangeController).addRange();
		verify(confirmPanel).set(metricConfiguration);
	}

	@Test
	public void shouldEditRange() throws Exception {
		RangeController rangeController = mockRangeController();
		Range range = new Range();
		controller.edit(range);
		verify(rangeController).editRange(range);
		verify(confirmPanel).set(metricConfiguration);
	}

	private RangeController mockRangeController() throws Exception {
		RangeController rangeController = PowerMockito.mock(RangeController.class);
		PowerMockito.when(confirmPanel.get()).thenReturn(metricConfiguration);
		PowerMockito.whenNew(RangeController.class).withArguments(metricConfiguration).thenReturn(rangeController);
		return rangeController;
	}
}