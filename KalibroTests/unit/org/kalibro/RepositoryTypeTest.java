package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryType.*;

import java.util.SortedSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.tests.EnumerationTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class RepositoryTypeTest extends EnumerationTest<RepositoryType> {

	@Override
	protected Class<RepositoryType> enumerationClass() {
		return RepositoryType.class;
	}

	@Override
	protected String expectedText(RepositoryType type) {
		return (type == CVS) ? "CVS" : super.expectedText(type);
	}

	@Test
	public void shouldAnswerIfIsLocal() {
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

	@Test
	public void shouldGetSupportedTypes() {
		RepositoryDao dao = mock(RepositoryDao.class);
		SortedSet<RepositoryType> supportedTypes = mock(SortedSet.class);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryDao()).thenReturn(dao);
		when(dao.supportedTypes()).thenReturn(supportedTypes);

		assertSame(supportedTypes, RepositoryType.supportedTypes());
	}
}