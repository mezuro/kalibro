package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.RepositoryType.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.loaders.*;

public class RepositoryTypeTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		RepositoryType.values();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("Local directory", "" + LOCAL_DIRECTORY);
		assertEquals("Local tarball", "" + LOCAL_TARBALL);
		assertEquals("Local zip", "" + LOCAL_ZIP);

		assertEquals("Bazaar", "" + BAZAAR);
		assertEquals("CVS", "" + CVS);
		assertEquals("Git", "" + GIT);
		assertEquals("Mercurial", "" + MERCURIAL);
		assertEquals("Remote tarball", "" + REMOTE_TARBALL);
		assertEquals("Remote zip", "" + REMOTE_ZIP);
		assertEquals("Subversion", "" + SUBVERSION);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testIsLocal() {
		assertTrue(LOCAL_DIRECTORY.isLocal());
		assertTrue(LOCAL_TARBALL.isLocal());
		assertTrue(LOCAL_ZIP.isLocal());

		assertFalse(BAZAAR.isLocal());
		assertFalse(CVS.isLocal());
		assertFalse(GIT.isLocal());
		assertFalse(MERCURIAL.isLocal());
		assertFalse(REMOTE_TARBALL.isLocal());
		assertFalse(REMOTE_ZIP.isLocal());
		assertFalse(SUBVERSION.isLocal());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectLoader() {
		assertClassEquals(LocalDirectoryLoader.class, LOCAL_DIRECTORY.getProjectLoader());
		assertClassEquals(LocalTarballLoader.class, LOCAL_TARBALL.getProjectLoader());
		assertClassEquals(LocalZipLoader.class, LOCAL_ZIP.getProjectLoader());

		assertClassEquals(BazaarLoader.class, BAZAAR.getProjectLoader());
		assertClassEquals(CvsLoader.class, CVS.getProjectLoader());
		assertClassEquals(GitLoader.class, GIT.getProjectLoader());
		assertClassEquals(MercurialLoader.class, MERCURIAL.getProjectLoader());
		assertClassEquals(RemoteTarballLoader.class, REMOTE_TARBALL.getProjectLoader());
		assertClassEquals(RemoteZipLoader.class, REMOTE_ZIP.getProjectLoader());
		assertClassEquals(SubversionLoader.class, SUBVERSION.getProjectLoader());
	}
}