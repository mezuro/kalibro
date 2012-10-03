package org.kalibro.tests;

import static org.kalibro.core.Environment.logsDirectory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.rules.Timeout;
import org.kalibro.core.Environment;

public abstract class IntegrationTest extends UnitTest {

	@AfterClass
	public static void cleanLogs() throws IOException {
		FileUtils.cleanDirectory(logsDirectory());
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(8000);
	}

	protected File samplesDirectory() {
		return new File(Environment.dotKalibro(), "samples");
	}

	protected File projectsDirectory() {
		return new File(Environment.dotKalibro(), "projects");
	}

	protected File helloWorldDirectory() {
		return new File(projectsDirectory(), "HelloWorld-1.0");
	}
}