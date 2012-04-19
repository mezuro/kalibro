package org.kalibro.core.model.enums;

import java.io.File;

import org.kalibro.KalibroError;
import org.kalibro.core.loaders.ProjectLoader;
import org.kalibro.core.model.Repository;
import org.kalibro.core.util.Identifier;

public enum RepositoryType {

	LOCAL_DIRECTORY, LOCAL_TARBALL, LOCAL_ZIP, BAZAAR, CVS("CVS"), GIT, MERCURIAL, REMOTE_ZIP, REMOTE_TARBALL,
	SUBVERSION;

	private String name;
	private ProjectLoader loader;

	private RepositoryType() {
		this("");
		name = Identifier.fromConstant(name()).asText();
	}
	
	private RepositoryType(String name) {
		this.name = name;
		initializeLoader();
	}
	
	private void initializeLoader() {
		String className = Identifier.fromConstant(name()).asClassName() + "Loader";
		try {
			loader = (ProjectLoader) Class.forName("org.kalibro.core.loaders." + className).newInstance();
		} catch (Exception exception) {
			throw new KalibroError("Error creating loader for " + this, exception);
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isLocal() {
		return name().startsWith("LOCAL");
	}

	public boolean isSupported() {
		return loader.validate();
	}

	public void load(Repository repository, File loadDirectory) {
		loader.load(repository, loadDirectory);
	}
}