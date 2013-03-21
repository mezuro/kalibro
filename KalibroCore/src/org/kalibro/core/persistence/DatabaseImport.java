package org.kalibro.core.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private static final String END_OF_DROP_TABLES = "/* END OF DROP TABLES */";

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

	private List<String> importStatements() throws IOException {
		DatabaseSettings databaseSettings = KalibroSettings.load().getServerSettings().getDatabaseSettings();
		String resourceName = "/META-INF/" + databaseSettings.getDatabaseType() + ".sql";
		String script = IOUtils.toString(getClass().getResourceAsStream(resourceName));
		List<String> statements = new ArrayList<String>(Arrays.asList(script.split("\n\n")));
		int endFlag = statements.indexOf(END_OF_DROP_TABLES);
		if (Environment.testing())
			statements.remove(endFlag);
		else
			statements = statements.subList(endFlag + 1, statements.size());
		return statements;
	}
}