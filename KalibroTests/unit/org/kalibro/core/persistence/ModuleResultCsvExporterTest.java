package org.kalibro.core.persistence;

import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.ModuleResult;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class ModuleResultCsvExporterTest extends KalibroTestCase {

	private File file;
	private ModuleResult moduleResult;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(FileUtils.class);
		file = PowerMockito.mock(File.class);
		moduleResult = helloWorldClassResult();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkExportContents() throws IOException {
		new ModuleResultCsvExporter(moduleResult).exportTo(file);

		String expectedContents = IOUtils.toString(getClass().getResourceAsStream("HelloWorld.csv"));
		PowerMockito.verifyStatic();
		FileUtils.writeStringToFile(file, expectedContents);
	}
}