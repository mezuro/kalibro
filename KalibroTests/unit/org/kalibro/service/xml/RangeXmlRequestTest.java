package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

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
public class RangeXmlRequestTest extends XmlTest {

	private Reading reading;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ReadingDao.class, "get", 42L)).thenReturn(reading);
	}

	@Override
	protected Range loadFixture() throws Exception {
		Range range = (Range) super.loadFixture();
		reading = range.getReading();
		Whitebox.setInternalState(reading, "id", 42L);
		return range;
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
		assertNull(new RangeXmlRequest(new Range()).reading());
		verifyStatic(never());
		DaoLazyLoader.createProxy(any(Class.class), anyString(), any());
	}
}