package org.kalibro.core.persistence;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;

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
		DatabaseSettings databaseSettings = KalibroSettings.load().getServerSettings().getDatabaseSettings();
		String resourceName = "/META-INF/" + databaseSettings.getDatabaseType() + ".sql";
		String[] statements = IOUtils.toString(getClass().getResourceAsStream(resourceName)).split("\n\n");
		if (!Environment.testing())
			statements = Arrays.copyOfRange(statements, 3, statements.length);
		return statements;
	}
}