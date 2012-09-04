package org.kalibro.core.persistence;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

public class NullPersistenceLogger extends AbstractSessionLog {

	@Override
	public void log(SessionLogEntry sessionLogEntry) {
		return;
	}
}