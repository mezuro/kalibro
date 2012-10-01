package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuditEvent.class)
public class AuditAdapterTest extends UnitTest {

	@Test
	public void shouldDoNothing() {
		AuditEvent event = mock(AuditEvent.class);
		Throwable exception = mock(Throwable.class);
		AuditAdapter adapter = new AuditAdapter() {/* just for test */};
		adapter.auditStarted(event);
		adapter.fileStarted(event);
		adapter.addError(event);
		adapter.addException(event, exception);
		adapter.fileFinished(event);
		adapter.auditFinished(event);
		verifyZeroInteractions(event);
	}
}