package org.kalibro.desktop.configuration;

import org.junit.Test;
import org.kalibro.desktop.KalibroDesktopTestCase;

/**
 * Base tools should be saved automatically.
 * 
 * @author Carlos Morais
 */
public class VerifyingBaseTools extends KalibroDesktopTestCase {

	@Test
	public void shouldHaveBaseTools() {
		startKalibroFrame();

		fixture.menuItemWithPath("Configuration", "New").click();
		enterNewConfigurationName();

		fixture.panel("configuration").button("add").click();
		fixture.dialog().radioButton("native").click();
		assertBaseTool(0, "Analizo");
		assertBaseTool(1, "Checkstyle");
	}

	private void assertBaseTool(int index, String name) {
		fixture.dialog().list("baseTools").selectItem(index);
		fixture.dialog().list("baseTools").requireSelection(name);
	}

	private void enterNewConfigurationName() {
		fixture.optionPane().requirePlainMessage();
		fixture.optionPane().requireTitle("New configuration");
		fixture.optionPane().requireMessage("Configuration name:");
		fixture.optionPane().textBox().setText("NEW");
		fixture.optionPane().okButton().click();
	}
}