package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.RepositoryType.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.loaders.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryType.class)
public class RepositoryTypeTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		RepositoryType.values();
		RepositoryType.valueOf("GIT");
	}

	private ProjectLoader loader;

	@Before
	public void setUp() {
		loader = PowerMockito.mock(ProjectLoader.class);
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
		assertClassEquals(LocalDirectoryLoader.class, getLoader(LOCAL_DIRECTORY));
		assertClassEquals(LocalTarballLoader.class, getLoader(LOCAL_TARBALL));
		assertClassEquals(LocalZipLoader.class, getLoader(LOCAL_ZIP));

		assertClassEquals(BazaarLoader.class, getLoader(BAZAAR));
		assertClassEquals(CvsLoader.class, getLoader(CVS));
		assertClassEquals(GitLoader.class, getLoader(GIT));
		assertClassEquals(MercurialLoader.class, getLoader(MERCURIAL));
		assertClassEquals(RemoteTarballLoader.class, getLoader(REMOTE_TARBALL));
		assertClassEquals(RemoteZipLoader.class, getLoader(REMOTE_ZIP));
		assertClassEquals(SubversionLoader.class, getLoader(SUBVERSION));
	}

	private ProjectLoader getLoader(RepositoryType type) {
		return Whitebox.getInternalState(type, "loader");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateProjectLoader() {
		Whitebox.setInternalState(GIT, ProjectLoader.class, loader);
		PowerMockito.when(loader.validate()).thenReturn(true);
		assertTrue(GIT.isSupported());
		PowerMockito.when(loader.validate()).thenReturn(false);
		assertFalse(GIT.isSupported());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadRepository() {
		Whitebox.setInternalState(GIT, ProjectLoader.class, loader);
		GIT.load(null, HELLO_WORLD_DIRECTORY);
		Mockito.verify(loader).load(null, HELLO_WORLD_DIRECTORY);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroErrorIfLoaderCouldNotBeCreated() {
		final RepositoryType type = PowerMockito.spy(GIT);
		PowerMockito.when(type.name()).thenReturn("INEXISTENT");
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				Whitebox.invokeMethod(type, "initializeLoader");
			}
		}, "Error creating loader for Git", ClassNotFoundException.class);
	}
}