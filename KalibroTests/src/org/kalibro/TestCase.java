package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.Timeout;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public abstract class TestCase extends TestShortcuts {

	@BeforeClass
	public static void setTestEnvironment() {
		Whitebox.setInternalState(Environment.class, "current", Environment.TEST);
	}

	@Rule
	public MethodRule testTimeout = testTimeout();

	private boolean waiting;

	protected Timeout testTimeout() {
		return new Timeout(Timeouts.UNIT_TIMEOUT);
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
		return yaml.loadAs(getClass().getResourceAsStream(name + ".yml"), type);
	}

	protected void checkException(Task task, Class<? extends Throwable> exceptionClass) {
		checkKalibroException(task, "Error while running task: null", exceptionClass);
	}

	protected void checkKalibroError(Task task, String message) {
		checkKalibroError(task, message, null);
	}

	protected void checkKalibroError(Task task, String message, Class<? extends Throwable> causeClass) {
		try {
			task.executeAndWait();
			fail("Did not throw expected error");
		} catch (KalibroException exception) {
			assertClassEquals(KalibroError.class, exception.getCause());
			KalibroError error = (KalibroError) exception.getCause();
			assertEquals(message, error.getMessage());
			Throwable cause = error.getCause();
			if (causeClass == null)
				assertNull("Unexpected cause: " + cause, cause);
			else
				assertClassEquals(causeClass, cause);
		}
	}

	protected void checkKalibroException(Task task, String message) {
		checkKalibroException(task, message, null);
	}

	protected void checkKalibroException(Task task, String message, Class<? extends Throwable> causeClass) {
		try {
			task.executeAndWait();
			fail("Did not throw expected exception");
		} catch (KalibroException exception) {
			assertEquals(message, exception.getMessage());
			Throwable cause = exception.getCause();
			if (causeClass == null)
				assertNull("Unexpected cause: " + cause, cause);
			else
				assertClassEquals(causeClass, cause);
		}
	}
}