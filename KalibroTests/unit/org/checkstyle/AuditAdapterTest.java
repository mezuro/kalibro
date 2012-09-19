package org.checkstyle;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class AuditAdapterTest extends TestCase {

	private AuditAdapter adapter;

	@Before
	public void setUp() {
		adapter = PowerMockito.spy(new MyAuditAdapter());
	}

	@Test
	public void shouldDoNothingOnAuditStarted() {
		adapter.auditStarted(null);
		Mockito.verify(adapter).auditStarted(null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void shouldDoNothingOnAuditFinished() {
		adapter.auditFinished(null);
		Mockito.verify(adapter).auditFinished(null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void shouldDoNothingOnFileStarted() {
		adapter.fileStarted(null);
		Mockito.verify(adapter).fileStarted(null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void shouldDoNothingOnFileFinished() {
		adapter.fileFinished(null);
		Mockito.verify(adapter).fileFinished(null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void shouldDoNothingOnAddError() {
		adapter.addError(null);
		Mockito.verify(adapter).addError(null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void shouldDoNothingOnAddException() {
		adapter.addException(null, null);
		Mockito.verify(adapter).addException(null, null);
		Mockito.verifyNoMoreInteractions(adapter);
	}

	private class MyAuditAdapter extends AuditAdapter {

		protected MyAuditAdapter() {
			super();
		}
	}
}