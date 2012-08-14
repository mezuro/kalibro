package org.kalibro.desktop;

import static org.powermock.api.mockito.PowerMockito.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.desktop.swingextension.InternalFrame;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.dialog.InputDialog;
import org.kalibro.desktop.swingextension.dialog.MessageDialog;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrudController.class, JOptionPane.class})
public class CrudControllerTest extends KalibroTestCase {

	private static final String NAME = "CrudControllerTest name";
	private static final List<String> NAMES = Arrays.asList(NAME);

	private InternalFrame<BaseTool> frame;
	private JDesktopPane desktopPane;
	private BaseTool entity;

	private BaseToolController controller, mock;

	@Before
	public void setUp() {
		prepareMocks();
		controller = new BaseToolController(desktopPane, mock);
	}

	private void prepareMocks() {
		entity = mock(BaseTool.class);
		frame = mock(InternalFrame.class);
		mock = mock(BaseToolController.class);
		desktopPane = mock(JDesktopPane.class);

		when(entity.toString()).thenReturn(NAME);
		when(frame.get()).thenReturn(entity);
		when(frame.getLocation()).thenReturn(new Point(0, 0));
		when(desktopPane.getSelectedFrame()).thenReturn(frame);

		when(mock.getEntityNames()).thenReturn(NAMES);
		when(mock.getEntity(NAME)).thenReturn(entity);
		when(mock.createEntity(NAME)).thenReturn(entity);
		when(mock.createFrameFor(entity)).thenReturn(frame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAddFrameIfUserDoesNotTypeEntityName() throws Exception {
		prepareInputDialog("New base tool", false);
		controller.newEntity();
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAddFrameIfEntityNameAlreadyExists() throws Exception {
		prepareInputDialog("New base tool", true);
		MessageDialog messageDialog = prepareMessageDialog("Base tool exists");

		controller.newEntity();
		verify(messageDialog).show("Base tool '" + NAME + "' already exists");
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFrameIfUserTypesValidEntityName() throws Exception {
		prepareInputDialog("New base tool", true);
		when(mock.getEntityNames()).thenReturn(new ArrayList<String>());
		controller.newEntity();
		verify(mock).createEntity(NAME);
		verifyFrameAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenIfUserDoesNotChooseEntity() throws Exception {
		prepareChoiceDialog("Open base tool", false);
		controller.open();
		verifyNever(mock).getEntity(NAME);
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenEntity() throws Exception {
		prepareChoiceDialog("Open base tool", true);
		controller.open();
		verify(mock).getEntity(NAME);
		verifyFrameAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotRemoveIfUserDoesNotChooseEntity() throws Exception {
		prepareChoiceDialog("Delete base tool", false);
		controller.delete();
		verifyNever(mock).removeEntity(NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveEntity() throws Exception {
		prepareChoiceDialog("Delete base tool", true);
		controller.delete();
		verify(mock).removeEntity(NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowMessageIfNoEntityExists() throws Exception {
		when(mock.getEntityNames()).thenReturn(new ArrayList<String>());
		MessageDialog messageDialog = prepareMessageDialog("No base tool");

		controller.open();
		verify(messageDialog).show("No base tool found");
		verifyFrameNerverAdded();

		Mockito.reset(messageDialog);

		controller.delete();
		verify(messageDialog).show("No base tool found");
		verifyNever(mock).removeEntity(NAME);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSelectedEntity() {
		controller.save();
		verify(mock).save(entity);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveWithOtherNameIfUserDoesNotTypeNewName() throws Exception {
		prepareInputDialog("Save base tool as...", false);
		controller.saveAs();
		verifyNever(mock).setEntityName(entity, NAME);
		verifyNever(mock).save(entity);
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveWithOtherName() throws Exception {
		prepareInputDialog("Save base tool as...", true);
		controller.saveAs();
		InOrder order = Mockito.inOrder(mock);
		order.verify(mock).setEntityName(entity, NAME);
		order.verify(mock).save(entity);
		verifyFrameAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFirstFrameOnTopLeft() throws Exception {
		when(desktopPane.getSelectedFrame()).thenReturn(null);
		Whitebox.invokeMethod(controller, "addFrameFor", entity);
		verify(frame).setLocation(new Point(0, 0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddNewFrameOnNewLocation() throws Exception {
		Whitebox.invokeMethod(controller, "addFrameFor", entity);
		verify(frame).setLocation(new Point(20, 20));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldJustCloseFrameIfUnmodified() {
		when(entity.deepEquals(entity)).thenReturn(true);
		controller.close();
		verifyNever(mock).save(entity);
		verify(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveNeitherCloseOnCancel() throws Exception {
		when(entity.deepEquals(entity)).thenReturn(false);
		prepareConfirmDialog(JOptionPane.CANCEL_OPTION);
		controller.close();
		verifyNever(mock).save(entity);
		verifyNever(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndCloseOnYes() throws Exception {
		when(entity.deepEquals(entity)).thenReturn(false);
		prepareConfirmDialog(JOptionPane.YES_OPTION);
		controller.close();
		verify(mock).save(entity);
		verify(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveButCloseOnNo() throws Exception {
		when(entity.deepEquals(entity)).thenReturn(false);
		prepareConfirmDialog(JOptionPane.NO_OPTION);
		controller.close();
		verifyNever(mock).save(entity);
		verify(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseSelectedFrameOnClosingEvent() {
		when(entity.deepEquals(entity)).thenReturn(true);
		controller.internalFrameClosing(null);
		verify(entity).deepEquals(entity);
		verify(frame).dispose();
	}

	private InputDialog prepareInputDialog(String title, boolean typed) throws Exception {
		InputDialog dialog = mock(InputDialog.class);
		whenNew(InputDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		when(dialog.userTyped("Base tool name:")).thenReturn(typed);
		when(dialog.getInput()).thenReturn(NAME);
		return dialog;
	}

	private MessageDialog prepareMessageDialog(String title) throws Exception {
		MessageDialog dialog = mock(MessageDialog.class);
		whenNew(MessageDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		return dialog;
	}

	private void prepareChoiceDialog(String title, boolean choose) throws Exception {
		ChoiceDialog<String> dialog = mock(ChoiceDialog.class);
		whenNew(ChoiceDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		when(dialog.choose("Select base tool:", NAMES)).thenReturn(choose);
		when(dialog.getChoice()).thenReturn(NAME);
	}

	private void prepareConfirmDialog(int option) throws Exception {
		String message = "Base tool '" + NAME + "' has been modified. Save changes?";
		mockStatic(JOptionPane.class);
		doReturn(option).when(JOptionPane.class,
			"showConfirmDialog", frame, message, "Save base tool", JOptionPane.YES_NO_CANCEL_OPTION);
	}

	private void verifyFrameNerverAdded() {
		verifyNever(mock).createFrameFor(entity);
		verifyNever(desktopPane).add(frame);
	}

	private void verifyFrameAdded() {
		verify(mock).createFrameFor(entity);
		verify(desktopPane).add(frame);
	}

	private <T> T verify(T aMock) {
		return Mockito.verify(aMock);
	}

	private <T> T verifyNever(T aMock) {
		return Mockito.verify(aMock, Mockito.never());
	}
}