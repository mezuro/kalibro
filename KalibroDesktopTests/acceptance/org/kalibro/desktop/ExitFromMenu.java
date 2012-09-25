package org.kalibro.desktop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Application should exit after click on menu Kalibro->Exit.
 * 
 * @author Carlos Morais
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.*", "javax.*", "org.xml.*"})
@PrepareOnlyThisForTest(KalibroMenu.class)
public class ExitFromMenu extends KalibroDesktopAcceptanceTest {

	@Test
	public void shouldExitFromMenu() {
		mockStatic(System.class);
		startKalibroFrame();

		fixture.menuItem("exit").click();
		verifyStatic();
		System.exit(0);
	}
}