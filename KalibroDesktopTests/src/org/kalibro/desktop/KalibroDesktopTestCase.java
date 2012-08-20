package org.kalibro.desktop;

import java.io.File;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;
import org.junit.After;
import org.kalibro.KalibroSettings;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.Task;

public class KalibroDesktopTestCase extends KalibroTestCase {

	protected WindowFixture<?> fixture;

	@After
	public void tearDown() {
		fixture.cleanUp();
	}

	protected void startFromMain() throws Exception {
		new File(Environment.dotKalibro(), "kalibro.settings").delete();
		new Task() {

			@Override
			public void perform() {
				KalibroDesktop.main(null);
			}
		}.executeInBackground();
		Thread.sleep(1500);
		fixture = new DialogFixture(BasicRobot.robotWithCurrentAwtHierarchy(), "settings");
	}

	protected void startKalibroFrame() {
		new KalibroSettings().save();
		KalibroFrame kalibroFrame = new KalibroFrame();
		fixture = new FrameFixture(kalibroFrame);
		((FrameFixture) fixture).show(kalibroFrame.getSize());
	}
}