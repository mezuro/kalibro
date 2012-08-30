package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.Task;
import org.yaml.snakeyaml.constructor.ConstructorException;

public class SettingsAcceptanceTest extends AcceptanceTest {

	private KalibroSettings settings;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		settingsFile.delete();
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void checkDefaultSettings() {
		assertFalse(settings.clientSide());
		checkClientSettings();
		checkServerSettings();
	}

	private void checkClientSettings() {
		ClientSettings clientSettings = settings.getClientSettings();
		assertEquals("http://localhost:8080/KalibroService/", clientSettings.getServiceAddress());
	}

	private void checkServerSettings() {
		ServerSettings serverSettings = settings.getServerSettings();
		assertEquals(new File(dotKalibro(), "repositories"), serverSettings.getLoadDirectory());

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
	public void settingsFileShouldBeHumanReadable() throws IOException {
		settings.save();
		String expected = loadResource("kalibroSettings-default.yml");
		expected = expected.replace("~/.kalibro", dotKalibro().getPath());
		assertEquals(expected, FileUtils.readFileToString(settingsFile));
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenLoadingInexistentSettings() {
		shouldLoadWithError(FileNotFoundException.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldThrowExceptionWhenLoadingFromCorruptedSettingsFile() throws IOException {
		FileUtils.writeStringToFile(settingsFile, "something weird");
		shouldLoadWithError(ConstructorException.class);
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
		}, "Could not import kalibro settings from file: " + settingsFile, causeClass);
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
		}, "Could not export kalibro settings to file: " + settingsFile, IOException.class);
	}
}