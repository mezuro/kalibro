package org.kalibro.core.command;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class WaitProcessTaskTest extends KalibroTestCase {

	private Process process;
	private WaitProcessTask task;

	@Before
	public void setUp() {
		process = PowerMockito.mock(Process.class);
		task = new WaitProcessTask(process);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldWaitForProcess() throws InterruptedException {
		task.perform();
		Mockito.verify(process).waitFor();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDestroyProcessOnInterruptedError() throws InterruptedException {
		InterruptedException error = new InterruptedException("My InterruptedException");
		PowerMockito.when(process.waitFor()).thenThrow(error);

		checkException(task, InterruptedException.class, "My InterruptedException");
		Mockito.verify(process).destroy();
	}
}