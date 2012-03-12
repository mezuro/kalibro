package org.kalibro.desktop.configuration;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
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

	private Configuration configuration;
	private ConfigurationDao configurationDao;

	private JDesktopPane desktopPane;
	private ConfigurationFrame frame;

	private ConfigurationController controller;

	@Before
	public void setUp() throws Exception {
		mockConfigurationDao();
		mockFrame();
		desktopPane = PowerMockito.mock(JDesktopPane.class);
		controller = new ConfigurationController(desktopPane);
	}

	private void mockConfigurationDao() {
		configurationDao = PowerMockito.mock(ConfigurationDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getConfigurationDao()).thenReturn(configurationDao);
	}

	protected void mockFrame() throws Exception {
		configuration = PowerMockito.mock(Configuration.class);
		frame = PowerMockito.mock(ConfigurationFrame.class);
		PowerMockito.whenNew(ConfigurationFrame.class).withArguments(configuration).thenReturn(frame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddNewFrameForNewConfiguration() throws Exception {
		PowerMockito.whenNew(Configuration.class).withNoArguments().thenReturn(configuration);
		controller.newConfiguration();

		verify(desktopPane).add(frame);
		verify(frame).select();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFramesOnDifferentLocations() throws Exception {
		prepareSelectedFrame();
		PowerMockito.whenNew(Configuration.class).withNoArguments().thenReturn(configuration);

		controller.newConfiguration();
		verify(frame).setLocation(new Point(20, 20));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldOpenConfiguration() throws Exception {
		prepareChoiceDialog(true);
		controller.open();

		verify(desktopPane).add(frame);
		verify(frame).select();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveConfiguration() throws Exception {
		prepareChoiceDialog(true);
		controller.delete();
		verify(configurationDao).removeConfiguration("");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotOpenIfUserDoesNotChooseConfiguration() throws Exception {
		prepareChoiceDialog(false);
		controller.open();

		verify(configurationDao, never()).getConfiguration(anyString());
		verify(desktopPane, never()).add(frame);
		verify(frame, never()).select();
	}

	private void prepareChoiceDialog(boolean choose) throws Exception {
		List<String> names = Arrays.asList("");
		ChoiceDialog<String> dialog = PowerMockito.mock(ChoiceDialog.class);
		PowerMockito.when(configurationDao.getConfigurationNames()).thenReturn(names);
		PowerMockito.whenNew(ChoiceDialog.class).withArguments("Choose configuration", desktopPane).thenReturn(dialog);
		PowerMockito.when(dialog.choose("Select configuration:", names)).thenReturn(choose);
		if (choose) {
			String chosen = names.get(0);
			PowerMockito.when(dialog.getChoice()).thenReturn(chosen);
			PowerMockito.when(configurationDao.getConfiguration(chosen)).thenReturn(configuration);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowMessageForNoConfiguration() throws Exception {
		MessageDialog messageDialog = prepareMessageDialog();
		controller.open();

		verify(messageDialog).show("No configuration found");
		verify(configurationDao, never()).getConfiguration(anyString());
		verify(desktopPane, never()).add(frame);
		verify(frame, never()).select();
	}

	private MessageDialog prepareMessageDialog() throws Exception {
		MessageDialog dialog = PowerMockito.mock(MessageDialog.class);
		PowerMockito.when(configurationDao.getConfigurationNames()).thenReturn(new ArrayList<String>());
		PowerMockito.whenNew(MessageDialog.class).withArguments("No configuration", desktopPane).thenReturn(dialog);
		return dialog;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSelectedConfiguration() {
		prepareSelectedFrame();
		controller.save();

		verify(configurationDao).save(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveConfigurationWithOtherName() throws Exception {
		prepareSelectedFrame();
		InputDialog dialog = prepareInputDialog(true);
		PowerMockito.when(dialog.getInput()).thenReturn("Other name");
		controller.saveAs();

		InOrder order = inOrder(configuration, configurationDao, desktopPane, frame);
		order.verify(configuration).setName("Other name");
		order.verify(configurationDao).save(configuration);
		order.verify(desktopPane).add(frame);
		order.verify(frame).select();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSaveIfUserDoesNotTypeNewName() throws Exception {
		prepareInputDialog(false);
		controller.saveAs();

		verify(configurationDao, never()).save(configuration);
		verify(desktopPane, never()).add(frame);
		verify(frame, never()).select();
	}

	private InputDialog prepareInputDialog(boolean typed) throws Exception {
		String title = "Save configuration as...";
		InputDialog dialog = PowerMockito.mock(InputDialog.class);
		PowerMockito.whenNew(InputDialog.class).withArguments(title, desktopPane).thenReturn(dialog);
		PowerMockito.when(dialog.userTyped("Configuration name:")).thenReturn(typed);
		return dialog;
	}

	private void prepareSelectedFrame() {
		Point existingLocation = new Point(0, 0);
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(frame);
		PowerMockito.when(frame.getLocation()).thenReturn(existingLocation);
		PowerMockito.when(frame.getConfiguration()).thenReturn(configuration);
	}
}