package org.kalibro.desktop.configuration;

import org.junit.Test;
import org.kalibro.desktop.KalibroDesktopTestCase;

/**
 * Database should be seeded automatically with the default configuration and the base tools.
 * 
 * @author Carlos Morais
 */
public class VerifyingSeeds extends KalibroDesktopTestCase {

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldBeSeeded() {
		startKalibroFrame();

		fixture.menuItemWithPath("Configuration", "Open").click();
		selectDefaultConfiguration();

		fixture.panel("configuration").button("add").click();
		fixture.dialog().radioButton("native").click();
		fixture.dialog().list("baseTools").selectItem(0);
		fixture.dialog().list("baseTools").requireSelection("Analizo");
	}

	private void selectDefaultConfiguration() {
		fixture.optionPane().requirePlainMessage();
		fixture.optionPane().requireTitle("Open configuration");
		fixture.optionPane().requireMessage("Select configuration:");
		fixture.optionPane().component().setSelectionValues(new Object[]{"Kalibro for Java"});
		fixture.optionPane().okButton().click();
	}
}