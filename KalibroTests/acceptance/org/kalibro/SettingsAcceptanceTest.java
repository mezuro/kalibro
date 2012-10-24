package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;
import org.yaml.snakeyaml.constructor.ConstructorException;

public class SettingsAcceptanceTest extends AcceptanceTest {

	private File settingsFile;
	private KalibroSettings settings;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		settingsFile = new File(dotKalibro(), "kalibro.settings");
		settingsFile.delete();
	}

	@Test
	public void checkDefaultSettings() {
		assertFalse(settings.clientSide());
		checkClientSettings();
		checkServerSettings();
		checkMailSettings();
	}

	private void checkMailSettings() {
		MailSettings mailSettings = settings.getMailSettings();
		assertEquals("smtp.gmail.com", mailSettings.getSmtpHost());
		assertEquals(465, mailSettings.getSmtpPort().intValue());
		assertEquals("example@gmail.com", mailSettings.getSenderMail());
		assertEquals("securepassword", mailSettings.getPassword());
	}

	private void checkClientSettings() {
		ClientSettings clientSettings = settings.getClientSettings();
		assertEquals("http://localhost:8080/KalibroService/", clientSettings.getServiceAddress());
	}

	private void checkServerSettings() {
		ServerSettings serverSettings = settings.getServerSettings();
		assertEquals(new File(dotKalibro(), "projects"), serverSettings.getLoadDirectory());

		DatabaseSettings databaseSettings = serverSettings.getDatabaseSettings();
		assertEquals(SupportedDatabase.MYSQL, databaseSettings.getDatabaseType());
		assertEquals("jdbc:mysql://localhost:3306/kalibro", databaseSettings.getJdbcUrl());
		assertEquals("kalibro", databaseSettings.getUsername());
		assertEquals("kalibro", databaseSettings.getPassword());
	}

	@Test
	public void shouldSaveLoadAndConfirmExistence() {
		assertFalse(KalibroSettings.exists());

		settings.save();
		assertTrue(KalibroSettings.exists());
		assertDeepEquals(settings, KalibroSettings.load());
	}

	@Test
	public void settingsFileShouldBeHumanReadable() throws IOException {
		settings.save();
		String expected = loadResource("KalibroSettings-default.yml");
		expected = expected.replace("~/.kalibro", dotKalibro().getPath());
		assertEquals(expected, FileUtils.readFileToString(settingsFile));
	}

	@Test
	public void shouldThrowExceptionWhenCannotLoadSettings() throws IOException {
		shouldLoadWithError(FileNotFoundException.class);

		FileUtils.writeStringToFile(settingsFile, "something weird");
		shouldLoadWithError(ConstructorException.class);

		settings.save();
		settingsFile.setReadable(false);
		shouldLoadWithError(FileNotFoundException.class);
	}

	private void shouldLoadWithError(Class<? extends Throwable> causeClass) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				KalibroSettings.load();
			}
		}).throwsException().withCause(causeClass)
			.withMessage("Could not import kalibro settings from file: " + settingsFile);
	}

	@Test
	public void shouldThrowExceptionWhenSavingOnNotWritableFile() {
		settings.save();
		settingsFile.setWritable(false);
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				settings.save();
			}
		}).throwsException().withCause(IOException.class)
			.withMessage("Could not export kalibro settings to file: " + settingsFile);
	}
}