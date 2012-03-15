package org.kalibro.desktop.configuration;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroDesktopTestCase;

/**
 * Database should be seeded automatically with the default configuration and the base tools.
 * 
 * @author Carlos Morais
 */
public class VerifyingSeeds extends KalibroDesktopTestCase {

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldBeSeeded() {
		startKalibroFrame();

		fixture.menuItem("configuration").click();
		fixture.menuItem("open").click();
		verifyChoiceDialogWithDefaultConfiguration();
		fixture.optionPane().okButton().click();

		fixture.panel("configuration").button("add").click();
		fixture.dialog().radioButton("native").click();
		fixture.dialog().list("baseTools").selectItem(0);
		fixture.dialog().list("baseTools").requireSelection("Analizo");
	}

	private void verifyChoiceDialogWithDefaultConfiguration() {
		fixture.optionPane().requirePlainMessage();
		fixture.optionPane().requireTitle("Open configuration");
		fixture.optionPane().requireMessage("Select configuration:");
		assertArrayEquals(new String[]{"Kalibro for Java"}, fixture.optionPane().component().getSelectionValues());
	}
}