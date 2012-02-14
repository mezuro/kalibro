package org.kalibro.core.model;

import static org.kalibro.core.model.enums.RepositoryType.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.RepositoryType;

public class RepositoryFixtures {

	private static Map<RepositoryType, String> addresses = initializeAddresses();

	private static Map<RepositoryType, String> initializeAddresses() {
		addresses = new HashMap<RepositoryType, String>();
		String testsPath = KalibroTestCase.TESTS_DIRECTORY.getAbsolutePath() + File.separator;
		addresses.put(LOCAL_DIRECTORY, testsPath + "HelloWorldDirectory/");
		addresses.put(LOCAL_TARBALL, testsPath + "HelloWorld.tar.gz");
		addresses.put(LOCAL_ZIP, testsPath + "HelloWorld.zip");
		addresses.put(BAZAAR, testsPath + "HelloWorldBazaar/");
		addresses.put(CVS, testsPath + "HelloWorldCvs/");
		addresses.put(GIT, testsPath + "HelloWorldGit/");
		addresses.put(MERCURIAL, testsPath + "HelloWorldMercurial/");
		addresses.put(REMOTE_TARBALL, "httpe:/invalid.address/HelloWorld.tar.gz");
		addresses.put(REMOTE_ZIP, "httpe://invalid.address/HelloWorld.zip");
		addresses.put(SUBVERSION, "file://" + testsPath + "HelloWorldSubversion");
		return addresses;
	}

	public static Repository helloWorldRepository(RepositoryType type) {
		return new Repository(type, addresses.get(type));
	}
}