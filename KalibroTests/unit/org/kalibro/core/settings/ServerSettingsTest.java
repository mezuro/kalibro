package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.core.util.Directories;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseTool.class)
public class ServerSettingsTest extends KalibroTestCase {

	private ServerSettings settings = new ServerSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertEquals(new File(Directories.kalibro(), "projects"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMapConstructor() {
		Map<?, ?> map = serverSettingsMap();
		settings = new ServerSettings(map);
		assertEquals(new File("/"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(databaseSettingsMap()), settings.getDatabaseSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkToString() throws IOException {
		String expected = IOUtils.toString(getClass().getResourceAsStream("server.settings"));
		expected = expected.replace("~", System.getProperty("user.home"));
		assertEquals(expected, "" + settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoadPathForProject() {
		Project project = ProjectFixtures.helloWorld();
		settings.setLoadDirectory(new File("/"));
		assertEquals(new File("/" + project.getName()), settings.getLoadDirectoryFor(project));
	}
}