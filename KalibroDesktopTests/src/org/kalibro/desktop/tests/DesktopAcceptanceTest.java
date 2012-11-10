package org.kalibro.desktop.tests;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;
import org.junit.After;
import org.junit.Before;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.tests.AcceptanceTest;

public abstract class DesktopAcceptanceTest extends AcceptanceTest {

	protected WindowFixture<?> fixture;

	@Before
	@SuppressWarnings("unused")
	public void setUp() throws Exception {
		KalibroFrame kalibroFrame = KalibroFrame.getInstance();
		fixture = new FrameFixture(kalibroFrame);
		((FrameFixture) fixture).show(kalibroFrame.getSize());
	}

	@After
	public void tearDown() {
		fixture.cleanUp();
	}
}