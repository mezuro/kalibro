package org.kalibro.core.model.enums;

import org.kalibro.KalibroException;
import org.kalibro.core.loaders.ProjectLoader;
import org.kalibro.core.util.Identifier;

public enum RepositoryType {

	LOCAL_DIRECTORY, LOCAL_TARBALL, LOCAL_ZIP, BAZAAR, CVS("CVS"), GIT, MERCURIAL, REMOTE_ZIP, REMOTE_TARBALL,
	SUBVERSION;

	private String name;

	private RepositoryType() {
		name = Identifier.fromConstant(name()).asText();
	}

	private RepositoryType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isLocal() {
		return name().startsWith("LOCAL");
	}

	public ProjectLoader getProjectLoader() {
		String className = Identifier.fromConstant(name()).asClassName() + "Loader";
		try {
			return (ProjectLoader) Class.forName("org.kalibro.core.loaders." + className).newInstance();
		} catch (Exception exception) {
			throw new KalibroException("Error creating loader for " + this, exception);
		}
	}
}