package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.AcceptanceTest;
import org.kalibro.Environment;
import org.kalibro.core.concurrent.Task;

public class KalibroSettingsAcceptanceTest extends AcceptanceTest {

	private KalibroSettings settings;

	@Before
	public void setUp() {
		settings = new KalibroSettings(kalibroSettingsMap());
		settingsFile.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void checkDefaultSettings() {
		settings = new KalibroSettings();
		assertFalse(settings.isClient());
		checkClientSettings();
		checkServerSettings();
	}

	private void checkClientSettings() {
		ClientSettings clientSettings = settings.getClientSettings();
		assertEquals("http://localhost:8080/KalibroService/", clientSettings.getServiceAddress());
	}

	private void checkServerSettings() {
		ServerSettings serverSettings = settings.getServerSettings();
		assertEquals(new File(Environment.dotKalibro(), "projects"), serverSettings.getLoadDirectory());

		DatabaseSettings databaseSettings = serverSettings.getDatabaseSettings();
		assertEquals(SupportedDatabase.MYSQL, databaseSettings.getDatabaseType());
		assertEquals("jdbc:mysql://localhost:3306/kalibro", databaseSettings.getJdbcUrl());
		assertEquals("kalibro", databaseSettings.getUsername());
		assertEquals("kalibro", databaseSettings.getPassword());
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldRetrieveIfSettingsExists() {
		assertFalse(KalibroSettings.exists());
		settings.save();
		assertTrue(KalibroSettings.exists());
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldSaveAndLoad() {
		settings.save();
		assertDeepEquals(settings, KalibroSettings.load());
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenLoadingInexistentSettings() {
		shouldLoadWithError(FileNotFoundException.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenLoadingFromCorruptedSettingsFile() throws IOException {
		settingsFile.createNewFile();
		shouldLoadWithError(NullPointerException.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenLoadingFromNotReadableSettingsFile() {
		settings.save();
		settingsFile.setReadable(false);
		shouldLoadWithError(FileNotFoundException.class);
	}

	private void shouldLoadWithError(Class<? extends Throwable> causeClass) {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				KalibroSettings.load();
			}
		}, "Could not load settings from file: " + settingsFile, causeClass);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenSavingOnNotWritableFile() {
		settings.save();
		settingsFile.setWritable(false);
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				settings.save();
			}
		}, "Could not save settings on file: " + settingsFile, IOException.class);
	}
}