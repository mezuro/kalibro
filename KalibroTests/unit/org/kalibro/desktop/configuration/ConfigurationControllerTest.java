package org.kalibro.desktop.configuration;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.dialog.InputDialog;
import org.kalibro.desktop.swingextension.dialog.MessageDialog;
import org.mockito.InOrder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationController.class, JOptionPane.class, Kalibro.class})
public class ConfigurationControllerTest extends KalibroTestCase {

	private ConfigurationDao dao;

	private JDesktopPane desktopPane;
	private ConfigurationFrame frame;
	private Configuration configuration;

	private ConfigurationController controller;

	@Before
	public void setUp() throws Exception {
		mockConfigurationDao();
		frame = PowerMockito.mock(ConfigurationFrame.class);
		desktopPane = PowerMockito.mock(JDesktopPane.class);
		configuration = PowerMockito.mock(Configuration.class);
		controller = PowerMockito.spy(new ConfigurationController(desktopPane));
		PowerMockito.doNothing().when(controller, "addFrameFor", configuration);
	}

	private void mockConfigurationDao() {
		dao = PowerMockito.mock(ConfigurationDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getConfigurationDao()).thenReturn(dao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAddFrameIfUserDoesNotTypeConfigurationName() throws Exception {
		prepareInputDialog("New configuration", false);

		controller.newConfiguration();
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAddFrameIfConfigurationNameAlreadyExists() throws Exception {
		String name = "My configuration";
		InputDialog inputDialog = prepareInputDialog("New configuration", true);
		PowerMockito.when(inputDialog.getInput()).thenReturn(name);
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(Arrays.asList(name));
		MessageDialog messageDialog = prepareMessageDialog("Configuration exists");

		controller.newConfiguration();
		verify(messageDialog).show("Configuration '" + name + "' already exists");
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFrameIfUserDoesTypeConfigurationName() throws Exception {
		InputDialog dialog = prepareInputDialog("New configuration", true);
		PowerMockito.when(dialog.getInput()).thenReturn("My configuration");
		PowerMockito.whenNew(Configuration.class).withNoArguments().thenReturn(configuration);

		controller.newConfiguration();
		verify(configuration).setName("My configuration");
		PowerMockito.verifyPrivate(controller).invoke("addFrameFor", configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenIfUserDoesNotChooseConfiguration() throws Exception {
		prepareChoiceDialog("Open configuration", false);

		controller.open();
		verify(dao, never()).getConfiguration(anyString());
		verifyFrameNerverAdded();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenConfigurationIfUserChooses() throws Exception {
		prepareChoiceDialog("Open configuration", true);

		controller.open();
		PowerMockito.verifyPrivate(controller).invoke("addFrameFor", configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotRemoveIfUserDoesNotChooseConfiguration() throws Exception {
		prepareChoiceDialog("Delete configuration", false);

		controller.delete();
		verify(dao, never()).removeConfiguration(anyString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfiguration() throws Exception {
		prepareChoiceDialog("Delete configuration", true);

		controller.delete();
		verify(dao).removeConfiguration("");
	}

	private void prepareChoiceDialog(String title, boolean choose) throws Exception {
		List<String> names = Arrays.asList("");
		ChoiceDialog<String> dialog = PowerMockito.mock(ChoiceDialog.class);
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(names);
		PowerMockito.whenNew(ChoiceDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		PowerMockito.when(dialog.choose("Select configuration:", names)).thenReturn(choose);
		if (choose) {
			String chosen = names.get(0);
			PowerMockito.when(dialog.getChoice()).thenReturn(chosen);
			PowerMockito.when(dao.getConfiguration(chosen)).thenReturn(configuration);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowMessageForNoConfiguration() throws Exception {
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(new ArrayList<String>());
		MessageDialog messageDialog = prepareMessageDialog("No configuration");

		controller.open();
		controller.delete();
		verify(messageDialog, times(2)).show("No configuration found");
		verifyFrameNerverAdded();
		verify(dao, never()).removeConfiguration(anyString());
	}

	private MessageDialog prepareMessageDialog(String title) throws Exception {
		MessageDialog dialog = PowerMockito.mock(MessageDialog.class);
		PowerMockito.whenNew(MessageDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		return dialog;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSelectedConfiguration() {
		prepareSelectedFrame();

		controller.save();
		verify(dao).save(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveWithOtherNameIfUserDoesNotTypeNewName() throws Exception {
		prepareInputDialog("Save configuration as...", false);

		controller.saveAs();
		verifyConfigurationNerverSaved();
		verifyFrameNerverAdded();
	}

	private void verifyFrameNerverAdded() throws Exception {
		PowerMockito.verifyPrivate(controller, never()).invoke("addFrameFor", any(Configuration.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveConfigurationWithOtherName() throws Exception {
		InputDialog dialog = prepareInputDialog("Save configuration as...", true);
		PowerMockito.when(dialog.getInput()).thenReturn("Other name");
		prepareSelectedFrame();

		controller.saveAs();
		InOrder order = inOrder(configuration, dao);
		order.verify(configuration).setName("Other name");
		order.verify(dao).save(configuration);
		PowerMockito.verifyPrivate(controller).invoke("addFrameFor", configuration);
	}

	private InputDialog prepareInputDialog(String title, boolean typed) throws Exception {
		InputDialog dialog = PowerMockito.mock(InputDialog.class);
		PowerMockito.whenNew(InputDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		PowerMockito.when(dialog.userTyped("Configuration name:")).thenReturn(typed);
		return dialog;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddNewFrameForConfiguration() throws Exception {
		ConfigurationFrame newFrame = prepareNewFrame();

		invokeAddFrame();
		verify(desktopPane).add(newFrame);
		verify(newFrame).addInternalFrameListener(controller);
		verify(newFrame).setLocation(new Point(0, 0));
		verify(newFrame).select();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddNewFrameOnNewLocation() throws Exception {
		prepareSelectedFrame();
		ConfigurationFrame newFrame = prepareNewFrame();

		invokeAddFrame();
		verify(newFrame).setLocation(new Point(20, 20));
	}

	private ConfigurationFrame prepareNewFrame() throws Exception {
		ConfigurationFrame newFrame = PowerMockito.mock(ConfigurationFrame.class);
		PowerMockito.whenNew(ConfigurationFrame.class).withArguments(configuration).thenReturn(newFrame);
		return newFrame;
	}

	private void invokeAddFrame() throws Exception {
		PowerMockito.doCallRealMethod().when(controller, "addFrameFor", configuration);
		Method addFrameFor = ConfigurationController.class.getDeclaredMethod("addFrameFor", Configuration.class);
		addFrameFor.setAccessible(true);
		addFrameFor.invoke(controller, configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCloseSelectedFrameOnClosingEvent() {
		PowerMockito.doNothing().when(controller).close();

		controller.internalFrameClosing(null);
		verify(controller).close();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldJustCloseFrameIfUnmodified() {
		prepareToClose(true);

		controller.close();
		verifyConfigurationNerverSaved();
		verify(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveNeitherCloseOnCancel() throws Exception {
		prepareToClose(false);
		prepareConfirmDialog(JOptionPane.CANCEL_OPTION);

		controller.close();
		verifyConfigurationNerverSaved();
		verify(frame, never()).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndCloseOnYes() throws Exception {
		prepareToClose(false);
		prepareConfirmDialog(JOptionPane.YES_OPTION);

		controller.close();
		verify(dao).save(configuration);
		verify(frame).dispose();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveButCloseOnNo() throws Exception {
		prepareToClose(false);
		prepareConfirmDialog(JOptionPane.NO_OPTION);

		controller.close();
		verifyConfigurationNerverSaved();
		verify(frame).dispose();
	}

	private void verifyConfigurationNerverSaved() {
		verify(dao, never()).save(any(Configuration.class));
	}

	private void prepareToClose(boolean unmodified) {
		prepareSelectedFrame();
		PowerMockito.when(configuration.getName()).thenReturn("");
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(Arrays.asList(""));
		PowerMockito.when(dao.getConfiguration("")).thenReturn(configuration);
		PowerMockito.when(configuration.deepEquals(configuration)).thenReturn(unmodified);
	}

	private void prepareSelectedFrame() {
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(frame);
		PowerMockito.when(frame.getLocation()).thenReturn(new Point(0, 0));
		PowerMockito.when(frame.getConfiguration()).thenReturn(configuration);
	}

	private void prepareConfirmDialog(int option) throws Exception {
		String message = "Configuration '' has been modified. Save changes?";
		PowerMockito.mockStatic(JOptionPane.class);
		PowerMockito.doReturn(option).when(JOptionPane.class, "showConfirmDialog",
			frame, message, "Save configuration", JOptionPane.YES_NO_CANCEL_OPTION);
	}
}