package org.kalibro.desktop.settings;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.kalibro.ClientSettings;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.ServerSettings;
import org.kalibro.desktop.KalibroFrame;
import org.kalibro.desktop.tests.DesktopSettingsAcceptanceTest;

/**
 * When opening for the first time, the dialog for editing the settings should open showing the default settings.<br/>
 * If the user cancels, no file should be written, and the application should close.
 * 
 * @author Carlos Morais
 */
public class CancelFirstEditSettings extends DesktopSettingsAcceptanceTest {

	@Test
	public void cancelFirstEditSettings() {
		fixture.checkBox("client").requireNotSelected();
		verifyDefaultServerSettings();

		fixture.checkBox("client").click();
		fixture.checkBox("client").requireSelected();
		verifyDefaultClientSettings();

		fixture.checkBox("client").uncheck();
		fixture.textBox("path").click();
		fixture.textBox("path").deleteText();
		fixture.textBox("username").click();
		verifyErrorMessageForInexistentDirectory();

		fixture.button("cancel").click();
		assertFalse(KalibroSettings.exists());
		assertFalse(KalibroFrame.getInstance().isVisible());
	}

	private void verifyDefaultServerSettings() {
		ServerSettings settings = new ServerSettings();
		fixture.textBox("path").requireText(settings.getLoadDirectory().getAbsolutePath());
		verifyDefaultDatabaseSettings();
	}

	private void verifyDefaultDatabaseSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		fixture.comboBox("databaseType").requireSelection(settings.getDatabaseType().toString());
		fixture.textBox("jdbcUrl").requireText(settings.getJdbcUrl());
		fixture.textBox("username").requireText(settings.getUsername());
		fixture.textBox("password").requireText(settings.getPassword());
	}

	private void verifyDefaultClientSettings() {
		ClientSettings settings = new ClientSettings();
		fixture.textBox("serviceAddress").requireText(settings.getServiceAddress());
	}

	private void verifyErrorMessageForInexistentDirectory() {
		fixture.optionPane().requireErrorMessage();
		fixture.optionPane().requireTitle("Error");
		fixture.optionPane().requireMessage("\"\" is not a valid directory");
		fixture.optionPane().button().click();
	}
}