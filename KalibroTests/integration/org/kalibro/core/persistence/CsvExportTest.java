package org.kalibro.core.persistence;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.Environment;
import org.kalibro.core.model.ModuleResult;

public class CsvExportTest extends TestCase {

	private File csvFile;
	private ModuleResult moduleResult;

	@Before
	public void setUp() {
		csvFile = new File(Environment.dotKalibro(), "HelloWorld.csv");
		assertFalse(csvFile.exists());
		moduleResult = newHelloWorldClassResult();
		moduleResult.setConfiguration(newConfiguration("amloc", "cbo", "lcom4"));
	}

	@After
	public void tearDown() {
		csvFile.delete();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testExport() throws IOException {
		InputStream expectedCsv = getClass().getResourceAsStream("HelloWorld.csv");
		new ModuleResultCsvExporter(moduleResult).exportTo(csvFile);
		assertEquals(IOUtils.toString(expectedCsv), FileUtils.readFileToString(csvFile));
	}
}