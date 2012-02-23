package org.kalibro.core.model;

import static org.kalibro.core.model.enums.RepositoryType.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.RepositoryType;

public final class RepositoryFixtures {

	private static Map<RepositoryType, String> addresses = initializeAddresses();

	private static Map<RepositoryType, String> initializeAddresses() {
		addresses = new HashMap<RepositoryType, String>();
		File repositoriesDir = new File(KalibroTestCase.TESTS_DIRECTORY, "repositories");
		String repositoriesPath = repositoriesDir.getAbsolutePath() + File.separator;
		addresses.put(LOCAL_DIRECTORY, repositoriesPath + "HelloWorldDirectory/");
		addresses.put(LOCAL_TARBALL, repositoriesPath + "HelloWorld.tar.gz");
		addresses.put(LOCAL_ZIP, repositoriesPath + "HelloWorld.zip");
		addresses.put(BAZAAR, repositoriesPath + "HelloWorldBazaar/");
		addresses.put(CVS, repositoriesPath + "HelloWorldCvs/");
		addresses.put(GIT, repositoriesPath + "HelloWorldGit/");
		addresses.put(MERCURIAL, repositoriesPath + "HelloWorldMercurial/");
		addresses.put(REMOTE_TARBALL, "httpe:/invalid.address/HelloWorld.tar.gz");
		addresses.put(REMOTE_ZIP, "httpe://invalid.address/HelloWorld.zip");
		addresses.put(SUBVERSION, "file://" + repositoriesPath + "HelloWorldSubversion");
		return addresses;
	}

	public static Repository helloWorldRepository(RepositoryType type) {
		return new Repository(type, addresses.get(type));
	}

	private RepositoryFixtures() {
		// Utility class
	}
}