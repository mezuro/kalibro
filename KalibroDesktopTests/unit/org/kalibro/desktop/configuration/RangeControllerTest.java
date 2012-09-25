package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RangeController.class)
public class RangeControllerTest extends UnitTest {

	private RangePanel panel;
	private ErrorDialog errorDialog;
	private EditDialog<Range> rangeDialog;

	private MetricConfiguration configuration;
	private RangeController controller;

	@Before
	public void setUp() throws Exception {
		mockComponents();
		configuration = metricConfiguration("amloc");
		controller = new RangeController(configuration);
	}

	private void mockComponents() throws Exception {
		panel = mock(RangePanel.class);
		rangeDialog = mock(EditDialog.class);
		errorDialog = mock(ErrorDialog.class);
		whenNew(RangePanel.class).withNoArguments().thenReturn(panel);
		whenNew(EditDialog.class).withArguments("Range", panel).thenReturn(rangeDialog);
		whenNew(ErrorDialog.class).withArguments(rangeDialog).thenReturn(errorDialog);
	}

	@Test
	public void shouldListenToTheRangeDialog() {
		verify(rangeDialog).addListener(controller);
	}

	@Test
	public void dialogShouldNotBeResizable() {
		verify(rangeDialog).setResizable(false);
	}

	@Test
	public void shouldShowDialogWithNewRangeWhenAdding() {
		controller.addRange();
		verifyDialogEdit(new Range());
	}

	@Test
	public void shouldShowDialogWithRangeWhenEditing() {
		Range range = newRange("amloc", GOOD);
		controller.editRange(range);
		verifyDialogEdit(range);
	}

	private void verifyDialogEdit(Range range) {
		ArgumentCaptor<Range> captor = ArgumentCaptor.forClass(Range.class);
		verify(rangeDialog).edit(captor.capture());
		assertDeepEquals(range, captor.getValue());
	}

	@Test
	public void shouldConfirmValidRangeAddition() {
		Range newRange = new Range(Double.NEGATIVE_INFINITY, 0.0);
		controller.addRange();
		assertTrue(controller.dialogConfirm(newRange));
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test
	public void shouldConfirmValidRangeEdition() {
		Range oldRange = newRange("amloc", EXCELLENT);
		Range newRange = new Range(0.0, 5.0);
		controller.editRange(oldRange);
		assertTrue(controller.dialogConfirm(newRange));
		assertFalse(configuration.hasRangeFor(6.0));
		assertSame(newRange, configuration.getRangeFor(0.0));
	}

	@Test
	public void shouldNotConfirmConflictingRangeAndShowError() {
		controller.addRange();
		assertFalse(controller.dialogConfirm(new Range()));

		ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
		verify(errorDialog).show(captor.capture());
		assertClassEquals(KalibroException.class, captor.getValue());
	}

	@Test
	public void oldRangeShouldStillBeThereOnError() {
		Range oldRange = configuration.getRangeFor(0.0);
		controller.editRange(oldRange);
		assertFalse(controller.dialogConfirm(new Range()));
		assertSame(oldRange, configuration.getRangeFor(0.0));
	}
}