package org.kalibro;

import java.io.File;
import java.util.SortedSet;

import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.RepositoryLoader;
import org.kalibro.dao.DaoFactory;

public enum RepositoryType {

	BAZAAR, CVS("CVS"), GIT, LOCAL_DIRECTORY, LOCAL_TARBALL, LOCAL_ZIP, MERCURIAL, REMOTE_ZIP, REMOTE_TARBALL,
	SUBVERSION;

	public static SortedSet<RepositoryType> supportedTypes() {
		return DaoFactory.getRepositoryDao().supportedTypes();
	}

	private String name;
	private RepositoryLoader loader;

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
			loader = (RepositoryLoader) Class.forName("org.kalibro.core.loaders." + className).newInstance();
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

	public boolean supportsAuthentication() {
		return loader.supportsAuthentication();
	}

	public void load(Repository repository, File loadDirectory) {
		loader.load(repository, loadDirectory);
	}
}