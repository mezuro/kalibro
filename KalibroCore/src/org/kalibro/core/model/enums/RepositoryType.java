package org.kalibro.core.model.enums;

import org.kalibro.core.loaders.*;
import org.kalibro.core.util.Identifier;

public enum RepositoryType {

	LOCAL_DIRECTORY(new LocalDirectoryLoader()),
	LOCAL_TARBALL(new LocalTarballLoader()),
	LOCAL_ZIP(new LocalZipLoader()),
	BAZAAR(new BazaarLoader()),
	CVS(new CvsLoader()) {

		@Override
		public String toString() {
			return "CVS";
		}
	},
	GIT(new GitLoader()),
	MERCURIAL(new MercurialLoader()),
	REMOTE_ZIP(new RemoteZipLoader()),
	REMOTE_TARBALL(new RemoteTarballLoader()),
	SUBVERSION(new SubversionLoader());

	private ProjectLoader loader;

	private RepositoryType(ProjectLoader loader) {
		this.loader = loader;
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public boolean isLocal() {
		return name().startsWith("LOCAL");
	}

	public ProjectLoader getProjectLoader() {
		return loader;
	}
}