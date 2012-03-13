package org.kalibro.desktop.configuration;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDesktopPane;

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
@PrepareForTest({ConfigurationController.class, Kalibro.class})
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
		PowerMockito.verifyPrivate(controller, never()).invoke("addFrameFor", any(Configuration.class));
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
		prepareChoiceDialog(false);

		controller.open();
		verify(dao, never()).getConfiguration(anyString());
		PowerMockito.verifyPrivate(controller, never()).invoke("addFrameFor", any(Configuration.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenConfigurationIfUserChooses() throws Exception {
		prepareChoiceDialog(true);

		controller.open();
		PowerMockito.verifyPrivate(controller).invoke("addFrameFor", configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotRemoveIfUserDoesNotChooseConfiguration() throws Exception {
		prepareChoiceDialog(false);

		controller.delete();
		verify(dao, never()).removeConfiguration(anyString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfiguration() throws Exception {
		prepareChoiceDialog(true);

		controller.delete();
		verify(dao).removeConfiguration("");
	}

	private void prepareChoiceDialog(boolean choose) throws Exception {
		List<String> names = Arrays.asList("");
		ChoiceDialog<String> dialog = PowerMockito.mock(ChoiceDialog.class);
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(names);
		PowerMockito.whenNew(ChoiceDialog.class).withArguments("Choose configuration", desktopPane).thenReturn(dialog);
		PowerMockito.when(dialog.choose("Select configuration:", names)).thenReturn(choose);
		if (choose) {
			String chosen = names.get(0);
			PowerMockito.when(dialog.getChoice()).thenReturn(chosen);
			PowerMockito.when(dao.getConfiguration(chosen)).thenReturn(configuration);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowMessageForNoConfiguration() throws Exception {
		MessageDialog messageDialog = prepareMessageDialog();

		controller.open();
		controller.delete();
		verify(messageDialog, times(2)).show("No configuration found");
		PowerMockito.verifyPrivate(controller, never()).invoke("addFrameFor", any(Configuration.class));
		verify(dao, never()).removeConfiguration(anyString());
	}

	private MessageDialog prepareMessageDialog() throws Exception {
		MessageDialog dialog = PowerMockito.mock(MessageDialog.class);
		PowerMockito.when(dao.getConfigurationNames()).thenReturn(new ArrayList<String>());
		PowerMockito.whenNew(MessageDialog.class).withArguments("No configuration", desktopPane).thenReturn(dialog);
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
		verify(dao, never()).save(any(Configuration.class));
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

	private void prepareSelectedFrame() {
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(frame);
		PowerMockito.when(frame.getLocation()).thenReturn(new Point(0, 0));
		PowerMockito.when(frame.getConfiguration()).thenReturn(configuration);
	}

	private void invokeAddFrame() throws Exception {
		PowerMockito.doCallRealMethod().when(controller, "addFrameFor", configuration);
		Method addFrameFor = ConfigurationController.class.getDeclaredMethod("addFrameFor", Configuration.class);
		addFrameFor.setAccessible(true);
		addFrameFor.invoke(controller, configuration);
	}
}