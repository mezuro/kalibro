package org.kalibro.core.concurrent;

import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ThreadInterrupterTest extends TestCase {

	@Test
	public void shouldInterruptThread() {
		Thread thread = PowerMockito.mock(Thread.class);
		new ThreadInterrupter(thread).run();
		Mockito.verify(thread).interrupt();
	}
}