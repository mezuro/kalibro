package org.kalibro.tests;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.kalibro.core.Environment.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public abstract class IntegrationTest extends UnitTest {

	@AfterClass
	public static void cleanLogs() {
		FileUtils.deleteQuietly(logsDirectory());
		FileUtils.deleteQuietly(projectsDirectory());
	}

	protected static File samplesDirectory() {
		return new File(dotKalibro(), "samples");
	}

	protected static File repositoriesDirectory() {
		return new File(samplesDirectory(), "repositories");
	}

	protected static File projectsDirectory() {
		return new File(dotKalibro(), "projects");
	}

	@Override
	protected TestTimeout testTimeout() {
		return new TestTimeout(10, SECONDS);
	}

	protected <T> T loadYaml(String name, Class<T> type) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.loadAs(getClass().getResourceAsStream(name + ".yml"), type);
	}
}