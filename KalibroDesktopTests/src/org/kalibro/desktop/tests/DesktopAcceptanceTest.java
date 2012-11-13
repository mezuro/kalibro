package org.kalibro.desktop.tests;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;
import org.junit.After;
import org.junit.Before;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.tests.AcceptanceTest;

public abstract class DesktopAcceptanceTest extends AcceptanceTest {

	protected WindowFixture<?> fixture;

	private KalibroFrame frame = KalibroFrame.getInstance();

	@Before
	@SuppressWarnings("unused")
	public void setUp() throws Exception {
		fixture = new FrameFixture(frame);
		((FrameFixture) fixture).show(frame.getSize());
	}

	@After
	public void tearDown() {
		while (!frame.getSelectedTitle().equals(""))
			frame.removeSelectedTab();
		fixture.cleanUp();
	}
}