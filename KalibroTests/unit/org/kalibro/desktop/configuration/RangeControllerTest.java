package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.*;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RangeController.class)
public class RangeControllerTest extends KalibroTestCase {

	private RangePanel panel;
	private ErrorDialog errorDialog;
	private EditDialog<Range> rangeDialog;

	private MetricConfiguration configuration;
	private RangeController controller;

	@Before
	public void setUp() throws Exception {
		mockComponents();
		configuration = MetricConfigurationFixtures.configuration("amloc");
		controller = new RangeController(configuration);
	}

	private void mockComponents() throws Exception {
		panel = PowerMockito.mock(RangePanel.class);
		rangeDialog = PowerMockito.mock(EditDialog.class);
		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(RangePanel.class).withNoArguments().thenReturn(panel);
		PowerMockito.whenNew(EditDialog.class).withArguments("Range", panel).thenReturn(rangeDialog);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(rangeDialog).thenReturn(errorDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToTheRangeDialog() {
		verify(rangeDialog).addListener(controller);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dialogShouldBeNamed() {
		verify(rangeDialog).setName("rangeDialog");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void dialogShouldNotBeResizable() {
		verify(rangeDialog).setResizable(false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowDialogWithNewRangeWhenAdding() {
		controller.addRange();
		assertDeepEquals(new Range(), captureRangeFromPanel());
		verify(rangeDialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowDialogWithRangeWhenEditing() {
		Range range = RangeFixtures.amlocRange(RangeLabel.GOOD);
		controller.editRange(range);

		assertSame(range, captureRangeFromPanel());
		verify(rangeDialog).setVisible(true);
	}

	private Range captureRangeFromPanel() {
		ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
		verify(panel).set(captor.capture());
		return captor.getValue();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmValidRangeAddition() {
		Range newRange = new Range(Double.NEGATIVE_INFINITY, 0.0);
		controller.addRange();
		assertTrue(controller.dialogConfirm(newRange));
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfirmValidRangeEdition() {
		Range oldRange = RangeFixtures.amlocRange(RangeLabel.EXCELLENT);
		Range newRange = new Range(0.0, 5.0);
		controller.editRange(oldRange);
		assertTrue(controller.dialogConfirm(newRange));
		assertFalse(configuration.hasRangeFor(6.0));
		assertSame(newRange, configuration.getRangeFor(0.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotConfirmConflictingRangeAndShowError() {
		controller.addRange();
		assertFalse(controller.dialogConfirm(new Range()));
		verify(errorDialog).show(any(IllegalArgumentException.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void oldRangeShouldStillBeThereOnError() {
		Range oldRange = configuration.getRangeFor(0.0);
		controller.editRange(oldRange);
		assertFalse(controller.dialogConfirm(new Range()));
		assertSame(oldRange, configuration.getRangeFor(0.0));
	}
}