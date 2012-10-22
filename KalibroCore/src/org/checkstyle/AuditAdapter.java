package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

/**
 * An abstract adapter class for receiving {@link AuditEvent}s. The methods in this class are empty. This class exists
 * as convenience for creating {@link AuditListener} implementations.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
abstract class AuditAdapter implements AuditListener {

	@Override
	public void auditStarted(AuditEvent event) {
		return;
	}

	@Override
	public void fileStarted(AuditEvent event) {
		return;
	}

	@Override
	public void addError(AuditEvent event) {
		return;
	}

	@Override
	public void addException(AuditEvent event, Throwable exception) {
		return;
	}

	@Override
	public void fileFinished(AuditEvent event) {
		return;
	}

	@Override
	public void auditFinished(AuditEvent event) {
		return;
	}
}