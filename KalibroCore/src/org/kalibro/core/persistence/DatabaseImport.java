package org.kalibro.core.persistence;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.kalibro.KalibroException;

public class DatabaseImport extends SessionEventAdapter implements SessionCustomizer {

	@Override
	public void customize(Session session) throws Exception {
		session.getEventManager().addListener(this);
	}

	@Override
	public void postLogin(SessionEvent event) {
		try {
			importDatabase(event);
		} catch (Exception exception) {
			throw new KalibroException("Could not import database.", exception);
		}
	}

	private void importDatabase(SessionEvent event) throws IOException {
		UnitOfWork unitOfWork = event.getSession().acquireUnitOfWork();
		for (String statement : importStatements())
			unitOfWork.executeNonSelectingSQL(statement);
		unitOfWork.commit();
	}

	private String[] importStatements() throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream("/META-INF/kalibro.sql")).split(";");
	}
}