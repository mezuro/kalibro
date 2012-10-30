package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Range;
import org.kalibro.Reading;
import org.kalibro.dao.ReadingDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class RangeXmlTest extends XmlTest {

	private static final Long READING_ID = new Random().nextLong();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Whitebox.setInternalState(dto, "readingId", READING_ID);
		Reading reading = Whitebox.getInternalState(entity, "reading");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "get", READING_ID)).thenReturn(reading);
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("beginning", Double.class, true);
		assertElement("end", Double.class, true);
		assertElement("comments", String.class);
		assertElement("readingId", Long.class);
	}

	@Test
	public void checkNullReading() {
		assertNull(new RangeXml(new Range()).reading());
	}
}