package org.kalibro.desktop.reading;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReadingController.class)
public class ReadingControllerTest extends UnitTest {

	private Reading reading;
	private ReadingGroup group;
	private ReadingGroupPanel groupPanel;

	private ReadingController controller;

	@Before
	public void setUp() {
		reading = mock(Reading.class);
		group = mock(ReadingGroup.class);
		groupPanel = mock(ReadingGroupPanel.class);
		controller = new ReadingController(groupPanel);
		controller.setReadingGroup(group);
	}

	@Test
	public void shouldInitializeReadingGroup() {
		assertDeepEquals(new ReadingGroup(), new ReadingController(groupPanel).getReadingGroup());
	}

	@Test
	public void shouldShowPanelWithNewReadingOnAdd() {
		controller.add();
		verify(groupPanel).showReadingPanel("Add reading", new Reading());
	}

	@Test
	public void shouldShowPanelWithSelectedReadingOnEdit() {
		controller.edit(reading);
		verify(groupPanel).showReadingPanel("Edit reading", reading);
	}

	@Test
	public void shouldHideReadingPanelOnRemove() {
		controller.remove(reading);
		verify(groupPanel).hideReadingPanel();
		verify(group).removeReading(reading);
	}

	@Test
	public void shouldJustHideReadingPanelOnCancel() {
		performAction("cancel");
		verify(groupPanel).hideReadingPanel();
		verifyNoMoreInteractions(groupPanel);
		verifyZeroInteractions(group, reading);
	}

	@Test
	public void shouldAddOnAddConfirmation() {
		when(groupPanel.getReading()).thenReturn(reading);
		controller.add();
		performAction("ok");

		InOrder order = Mockito.inOrder(group, groupPanel);
		order.verify(group).addReading(reading);
		order.verify(groupPanel).set(group);
		order.verify(groupPanel).hideReadingPanel();
	}

	@Test
	public void shouldJustRefreshOnEditConfirmation() {
		when(groupPanel.getReading()).thenReturn(reading);
		controller.edit(reading);
		performAction("ok");

		verify(group, never()).addReading(reading);
		InOrder order = Mockito.inOrder(groupPanel);
		order.verify(groupPanel).set(group);
		order.verify(groupPanel).hideReadingPanel();
	}

	@Test
	public void shouldShowErrorDialogOnErrorEditing() throws Exception {
		RuntimeException error = mock(RuntimeException.class);
		when(groupPanel.getReading()).thenThrow(error);
		ErrorDialog errorDialog = mockErrorDialog();

		controller.edit(reading);
		performAction("ok");

		verify(errorDialog).show(error);
		verify(groupPanel, never()).hideReadingPanel();
	}

	private ErrorDialog mockErrorDialog() throws Exception {
		ErrorDialog errorDialog = mock(ErrorDialog.class);
		whenNew(ErrorDialog.class).withArguments(groupPanel).thenReturn(errorDialog);
		return errorDialog;
	}

	private void performAction(String action) {
		Component source = mock(Component.class);
		ActionEvent event = mock(ActionEvent.class);
		when(event.getSource()).thenReturn(source);
		when(source.getName()).thenReturn(action);
		controller.actionPerformed(event);
	}
}