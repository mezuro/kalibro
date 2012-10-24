package org.kalibro.core.concurrent;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class TaskListenerNotifierTest extends UnitTest {

	@Test
	public void shouldNotifyListener() {
		TaskReport<Void> report = mock(TaskReport.class);
		TaskListener<Void> listener = mock(TaskListener.class);
		new TaskListenerNotifier<Void>(report, listener).perform();
		verify(listener).taskFinished(report);
	}
}