package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.RepositoryType.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.loaders.*;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryType.class)
public class RepositoryTypeTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() {
		RepositoryType.values();
		RepositoryType.valueOf("GIT");
	}

	@After
	public void setUp() {
		setInternalState(GIT, ProjectLoader.class, new GitLoader());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveSupportedTypes() {
		RepositoryType supported = mockType(true);
		RepositoryType unsupported = mockType(false);
		spy(RepositoryType.class);
		when(RepositoryType.values()).thenReturn(new RepositoryType[]{supported, unsupported});
		assertDeepEquals(RepositoryType.supportedTypes(), supported);
	}

	private RepositoryType mockType(boolean supported) {
		RepositoryType type = mock(RepositoryType.class);
		when(type.isSupported()).thenReturn(supported);
		return type;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkToString() {
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
	public void checkLocal() {
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
	public void checkProjectLoader() {
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
		return getInternalState(type, "loader");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveAuthenticationSupport() {
		ProjectLoader loader = mockGitLoader();
		when(loader.supportsAuthentication()).thenReturn(false);
		assertFalse(GIT.supportsAuthentication());

		when(loader.supportsAuthentication()).thenReturn(true);
		assertTrue(GIT.supportsAuthentication());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateProjectLoader() {
		ProjectLoader loader = mockGitLoader();
		when(loader.validate()).thenReturn(true);
		assertTrue(GIT.isSupported());
		when(loader.validate()).thenReturn(false);
		assertFalse(GIT.isSupported());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadRepository() {
		ProjectLoader loader = mockGitLoader();
		GIT.load(null, HELLO_WORLD_DIRECTORY);
		Mockito.verify(loader).load(null, HELLO_WORLD_DIRECTORY);
	}

	private ProjectLoader mockGitLoader() {
		ProjectLoader loader = mock(ProjectLoader.class);
		setInternalState(GIT, ProjectLoader.class, loader);
		return loader;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroErrorIfLoaderCouldNotBeCreated() {
		final RepositoryType type = spy(GIT);
		when(type.name()).thenReturn("INEXISTENT");
		checkKalibroError(new Task() {

			@Override
			protected void perform() throws Throwable {
				invokeMethod(type, "initializeLoader");
			}
		}, "Error creating loader for Git", ClassNotFoundException.class);
	}
}