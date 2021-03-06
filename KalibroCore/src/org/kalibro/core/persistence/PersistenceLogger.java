package org.kalibro.core.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.persistence.logging.DefaultSessionLog;
import org.kalibro.core.Environment;

/**
 * JPA logger that prints on Kalibro logs directory.
 * 
 * @author Carlos Morais
 */
public class PersistenceLogger extends DefaultSessionLog {

	public PersistenceLogger() throws IOException {
		super(new FileWriter(new File(Environment.logsDirectory(), "JPA.log")));
	}
}