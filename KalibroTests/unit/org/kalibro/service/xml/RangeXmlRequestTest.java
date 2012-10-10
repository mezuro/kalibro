package org.kalibro.service.xml;

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
public class RangeXmlRequestTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Reading reading = ((Range) entity).getReading();
		Whitebox.setInternalState(reading, "id", 42L);
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "get", 42L)).thenReturn(reading);
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("beginning", Double.class, true);
		assertElement("end", Double.class, true);
		assertElement("comments", String.class);
		assertElement("readingId", Long.class);
	}
}