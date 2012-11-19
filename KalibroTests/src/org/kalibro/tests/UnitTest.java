package org.kalibro.tests;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.Timeout;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public abstract class UnitTest extends SpecialAssertions {

	public static <T> T loadFixture(String name, Class<T> type) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		String resourceName = type.getSimpleName() + "-" + name + ".yml";
		return yaml.loadAs(type.getResourceAsStream(resourceName), type);
	}

	@Rule
	public MethodRule testTimeout = testTimeout();

	protected Timeout testTimeout() {
		return new TestTimeout(2, SECONDS);
	}

	protected String loadResource(String name) throws IOException {
		return IOUtils.toString(getStream(name));
	}

	protected InputStream getStream(String name) {
		return getClass().getResourceAsStream(name);
	}

	protected File getFile(String name) throws Exception {
		return new File(getClass().getResource(name).toURI());
	}

	protected class TestTimeout extends Timeout {

		protected TestTimeout(long duration, TimeUnit timeUnit) {
			super(new Long(timeUnit.toMillis(duration)).intValue());
		}
	}
}