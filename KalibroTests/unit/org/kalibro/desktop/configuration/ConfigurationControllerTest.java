package org.kalibro.desktop.configuration;

import java.awt.Point;

import javax.swing.JDesktopPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Configuration;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationController.class)
public class ConfigurationControllerTest extends KalibroTestCase {

	private Configuration configuration;
	private JDesktopPane desktopPane;
	private ConfigurationFrame frame;

	private ConfigurationController controller;

	@Before
	public void setUp() throws Exception {
		prepareMocks();
		controller = new ConfigurationController(desktopPane);
	}

	private void prepareMocks() throws Exception {
		configuration = PowerMockito.mock(Configuration.class);
		desktopPane = PowerMockito.mock(JDesktopPane.class);
		frame = PowerMockito.mock(ConfigurationFrame.class);
		PowerMockito.whenNew(Configuration.class).withNoArguments().thenReturn(configuration);
		PowerMockito.whenNew(ConfigurationFrame.class).withArguments(configuration).thenReturn(frame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddNewFrameForNewConfiguration() throws Exception {
		controller.newConfiguration();

		PowerMockito.verifyNew(Configuration.class).withNoArguments();
		InOrder order = Mockito.inOrder(desktopPane, frame);
		order.verify(desktopPane).add(frame);
		order.verify(frame).select();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFramesOnDifferentLocations() {
		Point existingLocation = new Point(0, 0);
		PowerMockito.when(desktopPane.getSelectedFrame()).thenReturn(frame);
		PowerMockito.when(frame.getLocation()).thenReturn(existingLocation);

		controller.newConfiguration();
		Mockito.verify(frame).setLocation(new Point(20, 20));
	}
}