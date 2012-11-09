package org.kalibro.desktop.tests;

import java.io.File;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.fixture.DialogFixture;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.desktop.KalibroDesktop;
import org.kalibro.desktop.settings.KalibroSettingsPanel;
import org.kalibro.desktop.settings.SettingsController;
import org.kalibro.desktop.swingextension.dialog.EditDialog;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.*", "javax.*", "org.xml.*"})
@PrepareOnlyThisForTest(SettingsController.class)
public abstract class DesktopSettingsAcceptanceTest extends DesktopAcceptanceTest {

	@Override
	public void setUp() throws Exception {
		new File(Environment.dotKalibro(), "kalibro.settings").delete();

		KalibroSettingsPanel panel = new KalibroSettingsPanel();
		whenNew(KalibroSettingsPanel.class).withNoArguments().thenReturn(panel);
		EditDialog<KalibroSettings> dialog = new EditDialog<KalibroSettings>(null, "Kalibro Settings", panel);
		whenNew(EditDialog.class).withArguments(null, "Kalibro Settings", panel).thenReturn(dialog);

		runMain();
		fixture = new DialogFixture(BasicRobot.robotWithCurrentAwtHierarchy(), dialog);
	}

	private void runMain() throws InterruptedException {
		new VoidTask() {

			@Override
			protected void perform() {
				KalibroDesktop.main(null);
			}
		}.executeInBackground();
		Thread.sleep(1500);
	}
}