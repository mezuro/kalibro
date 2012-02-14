package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.*;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(RangeController.class)
public class RangeControllerTest extends KalibroTestCase {

	private RangeDialog dialog;
	private ErrorDialog errorDialog;

	private MetricConfiguration configuration;
	private RangeController controller;

	@Before
	public void setUp() throws Exception {
		mockDialogs();
		configuration = MetricConfigurationFixtures.configuration("amloc");
		controller = new RangeController(configuration);
	}

	private void mockDialogs() throws Exception {
		dialog = PowerMockito.mock(RangeDialog.class);
		PowerMockito.whenNew(RangeDialog.class).withNoArguments().thenReturn(dialog);

		errorDialog = PowerMockito.mock(ErrorDialog.class);
		PowerMockito.whenNew(ErrorDialog.class).withArguments(dialog).thenReturn(errorDialog);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldListenToTheRangeDialog() {
		verify(dialog).addOkListener(controller);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowDialogWithNewRangeWhenAdding() {
		controller.addRange();

		ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
		verify(dialog).setRange(captor.capture());

		assertDeepEquals(new Range(), captor.getValue());
		verify(dialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowDialogWithRangeWhenEditing() {
		Range range = RangeFixtures.amlocRange(RangeLabel.GOOD);
		controller.editRange(range);

		ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
		verify(dialog).setRange(captor.capture());

		assertDeepEquals(range, captor.getValue());
		verify(dialog).setVisible(true);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseDialogOnOk() {
		PowerMockito.when(dialog.getRange()).thenReturn(new Range(Double.NEGATIVE_INFINITY, 0.0));
		controller.actionPerformed(null);
		verify(dialog).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddRangeOnAdd() {
		Range range = new Range(Double.NEGATIVE_INFINITY, 0.0);
		PowerMockito.when(dialog.getRange()).thenReturn(range);
		controller.addRange();
		controller.actionPerformed(null);
		assertSame(range, configuration.getRangeFor(- 1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReplaceRangeOnEdit() {
		Range oldRange = RangeFixtures.amlocRange(RangeLabel.EXCELLENT);
		Range newRange = new Range(0.0, 5.0);
		PowerMockito.when(dialog.getRange()).thenReturn(newRange);
		controller.editRange(oldRange);
		controller.actionPerformed(null);
		assertFalse(configuration.hasRangeFor(6.0));
		assertSame(newRange, configuration.getRangeFor(0.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowErrorDialogOnError() {
		PowerMockito.when(dialog.getRange()).thenReturn(new Range());
		controller.actionPerformed(null);
		verify(errorDialog).show(any(IllegalArgumentException.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotCloseDialogOnError() {
		PowerMockito.when(dialog.getRange()).thenReturn(new Range());
		controller.actionPerformed(null);
		verify(dialog, never()).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void oldRangeShouldStillBeThereOnError() {
		Range oldRange = RangeFixtures.amlocRange(RangeLabel.EXCELLENT);
		PowerMockito.when(dialog.getRange()).thenReturn(new Range());
		controller.editRange(oldRange);
		controller.actionPerformed(null);
		assertSame(oldRange, configuration.getRangeFor(0.0));
	}
}