package org.kalibro.desktop.settings;

import static org.junit.Assert.assertTrue;

import org.fest.swing.fixture.FrameFixture;
import org.junit.Test;
import org.kalibro.KalibroSettings;
import org.kalibro.desktop.tests.KalibroDesktopAcceptanceTest;

/**
 * On the first execution, if the user edit the settings and confirms, the settings file should be written and the
 * application frame opened. The settings should still be available via menu.
 * 
 * @author Carlos Morais
 */
public class FirstEditSettings extends KalibroDesktopAcceptanceTest {

	@Test
	public void firstEditSettings() throws Exception {
		startFromMain();

		fixture.checkBox("client").requireNotSelected();

		fixture.checkBox("client").check();
		fixture.button("ok").click();
		assertTrue(KalibroSettings.exists());

		fixture.robot.waitForIdle();
		fixture = new FrameFixture(fixture.robot, "kalibroFrame");
		fixture.menuItem("settings").click();
		fixture = fixture.dialog();
		fixture.checkBox("client").requireSelected();
	}
}