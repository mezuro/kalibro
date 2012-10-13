package org.kalibro.core.loaders;

import static org.kalibro.RepositoryType.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Environment;

@Deprecated
public final class RepositoryFixtures {

	private static Map<RepositoryType, String> addresses;
	private static Map<RepositoryType, Repository> repositories;

	static {
		addresses = new HashMap<RepositoryType, String>();
		repositories = new HashMap<RepositoryType, Repository>();
		File samplesDir = new File(Environment.dotKalibro(), "samples");
		File repositoriesDir = new File(samplesDir, "repositories");
		String repositoriesPath = repositoriesDir.getAbsolutePath() + File.separator;
		newAddress(MERCURIAL, repositoriesPath + "HelloWorldMercurial/");
		newAddress(REMOTE_TARBALL, "http:/invalid.address/HelloWorld.tar.gz");
		newAddress(REMOTE_ZIP, "http://invalid.address/HelloWorld.zip");
		newAddress(SUBVERSION, "file://" + repositoriesPath + "HelloWorldSubversion");
	}

	private static void newAddress(RepositoryType type, String address) {
		addresses.put(type, address);
		repositories.put(type, newHelloWorldRepository(type));
	}

	public static Repository helloWorldRepository(RepositoryType type) {
		return repositories.get(type);
	}

	public static Repository newHelloWorldRepository(RepositoryType type) {
		return new Repository("" + type, type, addresses.get(type));
	}

	private RepositoryFixtures() {
		return;
	}
}