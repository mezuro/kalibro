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
import org.kalibro.desktop.swingextension.dialog.MessageDialog;
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
		Point existingLocation = new Point(0, 0);
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(frame);
		PowerMockito.when(frame.getLocation()).thenReturn(existingLocation);
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
	public void shouldRemoveOpenConfiguration() throws Exception {
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

		verify(messageDialog).show("No configuration found", "No configuration");
		verify(configurationDao, never()).getConfiguration(anyString());
		verify(desktopPane, never()).add(frame);
		verify(frame, never()).select();
	}

	private MessageDialog prepareMessageDialog() throws Exception {
		MessageDialog dialog = PowerMockito.mock(MessageDialog.class);
		PowerMockito.when(configurationDao.getConfigurationNames()).thenReturn(new ArrayList<String>());
		PowerMockito.whenNew(MessageDialog.class).withArguments(desktopPane).thenReturn(dialog);
		return dialog;
	}
}