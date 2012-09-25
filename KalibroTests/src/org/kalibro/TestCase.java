package org.kalibro;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.Timeout;
import org.kalibro.core.Environment;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.powermock.reflect.Whitebox;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public abstract class TestCase extends ExtendedAsserts {

	@BeforeClass
	public static void setTestEnvironment() {
		Whitebox.setInternalState(Environment.class, "current", Environment.TEST);
	}

	@Rule
	public MethodRule testTimeout = testTimeout();

	private boolean waiting;

	protected Timeout testTimeout() {
		return new Timeout(3000);
	}

	protected void waitNotification() throws InterruptedException {
		waiting = true;
		while (waiting)
			wait();
	}

	protected void notifyTest() {
		waiting = false;
		notify();
	}

	protected File getResource(String name) throws Exception {
		return new File(getClass().getResource(name).toURI());
	}

	protected String loadResource(String name) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(name));
	}

	protected <T> T loadFixture(String name, Class<T> type) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		String resourceName = type.getSimpleName() + "-" + name + ".yml";
		return yaml.loadAs(type.getResourceAsStream(resourceName), type);
	}

	protected <T> T verify(T mock) {
		return Mockito.verify(mock);
	}

	protected <T> T verify(T mock, VerificationMode mode) {
		return Mockito.verify(mock, mode);
	}

	protected VerificationMode never() {
		return Mockito.never();
	}

	protected VerificationMode once() {
		return Mockito.times(1);
	}

	protected VerificationMode times(int times) {
		return Mockito.times(times);
	}
}