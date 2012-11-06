package org.kalibro.desktop.tests;

import java.io.File;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;
import org.junit.After;
import org.junit.Before;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.desktop.KalibroDesktop;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.tests.AcceptanceTest;

public class DesktopAcceptanceTest extends AcceptanceTest {

	private static final int FIXTURE_WAIT = 1500;

	protected WindowFixture<?> fixture;

	@Before
	public void setUp() throws Exception {
		if (fromMain())
			startFromMain();
		else
			startKalibroFrame();
	}

	protected boolean fromMain() {
		return false;
	}

	private void startFromMain() throws Exception {
		new File(Environment.dotKalibro(), "kalibro.settings").delete();
		new VoidTask() {

			@Override
			protected void perform() {
				KalibroDesktop.main(null);
			}
		}.executeInBackground();
		Thread.sleep(FIXTURE_WAIT);
		fixture = new DialogFixture(BasicRobot.robotWithCurrentAwtHierarchy(), "settings");
	}

	private void startKalibroFrame() {
		KalibroFrame kalibroFrame = new KalibroFrame();
		fixture = new FrameFixture(kalibroFrame);
		((FrameFixture) fixture).show(kalibroFrame.getSize());
	}

	@After
	public void tearDown() {
		fixture.cleanUp();
	}
}