package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.*;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.panel.CardStackPanel;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(MetricConfigurationController.class)
public class MetricConfigurationControllerTest extends KalibroTestCase {

	private ErrorDialog errorDialog;
	private CardStackPanel cardStack;
	private AddMetricDialog addDialog;
	private MetricConfigurationPanel panel;

	private Configuration configuration;
	private RangeController rangeController;
	private MetricConfigurationController controller;

	@Before
	public void setUp() throws Exception {
		mockPanels();
		mockDialogs();
		mockRangeController();
		configuration = ConfigurationFixtures.simpleConfiguration();
		controller = new MetricConfigurationController(configuration, cardStack);
	}

	private void mockPanels() throws Exception {
		cardStack = PowerMockito.mock(CardStackPanel.class);
		panel = PowerMockito.mock(MetricConfigurationPanel.class);
		PowerMockito.whenNew(MetricConfigurationPanel.class).withNoArguments().thenReturn(panel);
	}

	private void mockDialogs() throws Exception {
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		addDialog = PowerMockito.mock(AddMetricDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(any()).thenReturn(errorDialog);
		PowerMockito.whenNew(AddMetricDialog.class).withNoArguments().thenReturn(addDialog);
	}

	private void mockRangeController() throws Exception {
		rangeController = PowerMockito.mock(RangeController.class);
		PowerMockito.whenNew(RangeController.class).withArguments(any(MetricConfiguration.class))
			.thenReturn(rangeController);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToThePanel() {
		verify(panel).addRangesPanelListener(controller);
		verify(panel).addButtonListener(controller);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotShowPanelIfUserDoesNotConfirmOnChooseNewMetric() {
		controller.addMetricConfiguration();
		verify(cardStack, never()).push(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotClosePanelOnErrorChoosingMetric() {
		NativeMetric amloc = NativeMetricFixtures.nativeMetric("amloc");
		chooseNewMetric(amloc);
		verify(errorDialog).show(any(IllegalArgumentException.class));
		verify(addDialog, never()).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowPanelWithNewConfigurationForChosenMetricWhenAdding() {
		Metric chosenMetric = new CompoundMetric();
		chooseNewMetric(chosenMetric);

		ArgumentCaptor<MetricConfiguration> captor = ArgumentCaptor.forClass(MetricConfiguration.class);
		verify(panel).set(captor.capture());
		assertDeepEquals(new MetricConfiguration(chosenMetric), captor.getValue());
		verify(cardStack).push(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowPanelWithMetricConfigurationWhenEditing() {
		MetricConfiguration metricConfiguration = MetricConfigurationFixtures.configuration("amloc");
		controller.edit(metricConfiguration);

		ArgumentCaptor<MetricConfiguration> captor = ArgumentCaptor.forClass(MetricConfiguration.class);
		verify(panel).set(captor.capture());
		assertDeepEquals(metricConfiguration, captor.getValue());
		verify(cardStack).push(panel);
	}

	@Test(timeout = 1000 * UNIT_TIMEOUT)
	public void shouldPopPanelOnCancel() {
		MetricConfiguration metricConfiguration = MetricConfigurationFixtures.configuration("amloc");
		controller.edit(metricConfiguration);
		clickButton("cancel");
		verify(cardStack).pop();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPopPanelOnOk() {
		MetricConfiguration metricConfiguration = new MetricConfiguration(new CompoundMetric());
		controller.edit(metricConfiguration);
		PowerMockito.when(panel.get()).thenReturn(metricConfiguration);
		clickButton("ok");
		verify(cardStack).pop();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMetricConfigurationOnAdd() {
		CompoundMetric metric = new CompoundMetric();
		MetricConfiguration metricConfiguration = new MetricConfiguration(metric);
		PowerMockito.when(panel.get()).thenReturn(metricConfiguration);

		chooseNewMetric(metric);
		clickButton("ok");
		assertSame(metricConfiguration, configuration.getConfigurationFor(metric));
	}

	private void chooseNewMetric(Metric chosenMetric) {
		controller.addMetricConfiguration();

		ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
		verify(addDialog).addOkListener(captor.capture());
		verify(addDialog).setVisible(true);

		PowerMockito.when(addDialog.getMetric()).thenReturn(chosenMetric);
		captor.getValue().actionPerformed(null);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReplaceMetricConfigurationOnEdit() {
		NativeMetric amloc = NativeMetricFixtures.nativeMetric("amloc");
		MetricConfiguration oldConfiguration = MetricConfigurationFixtures.configuration("amloc");
		MetricConfiguration newConfiguration = new MetricConfiguration(amloc);
		PowerMockito.when(panel.get()).thenReturn(newConfiguration);
		controller.edit(oldConfiguration);
		clickButton("ok");
		assertSame(newConfiguration, configuration.getConfigurationFor(amloc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowErrorDialogOnError() {
		editAmlocToCodeConflict();
		verify(errorDialog).show(any(IllegalArgumentException.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotPopPanelOnError() {
		editAmlocToCodeConflict();
		verify(cardStack, never()).pop();
	}

	private void editAmlocToCodeConflict() {
		NativeMetric amloc = NativeMetricFixtures.nativeMetric("amloc");
		MetricConfiguration metricConfiguration = configuration.getConfigurationFor(amloc);
		controller.edit(metricConfiguration);

		metricConfiguration = new MetricConfiguration(amloc);
		metricConfiguration.setCode("cbo");
		PowerMockito.when(panel.get()).thenReturn(metricConfiguration);
		clickButton("ok");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void oldConfigurationShouldStillBeThereOnError() {
		NativeMetric amloc = NativeMetricFixtures.nativeMetric("amloc");
		MetricConfiguration oldConfiguration = MetricConfigurationFixtures.configuration("amloc");
		MetricConfiguration newConfiguration = new MetricConfiguration(NativeMetricFixtures.nativeMetric("cbo"));
		PowerMockito.when(panel.get()).thenReturn(newConfiguration);
		controller.edit(oldConfiguration);
		clickButton("ok");
		assertSame(oldConfiguration, configuration.getConfigurationFor(amloc));
	}

	private void clickButton(String name) {
		ActionEvent event = PowerMockito.mock(ActionEvent.class);
		PowerMockito.when(event.getSource()).thenReturn(new Button(name, ""));
		controller.actionPerformed(event);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddRange() {
		controller.add();
		verify(rangeController).addRange();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEditRange() {
		Range range = new Range();
		controller.edit(range);
		verify(rangeController).editRange(range);
	}
}