package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class ReadingGroupXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		ReadingGroup group = (ReadingGroup) entity;
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "readingsOf", group.getId())).thenReturn(group.getReadings());
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
	}

	@Test
	public void readingsBeEmptyIfHasNoId() {
		assertTrue(new ReadingGroupXml().readings().isEmpty());
	}
}