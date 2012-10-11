package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.dto.DataTransferObject;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoLazyLoader.class, DataTransferObject.class})
public class MetricConfigurationXmlRequestTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mockLazyLoading();
		Set<Range> ranges = Whitebox.getInternalState(entity, "ranges");
		spy(DataTransferObject.class);
		doReturn(new TreeSet<Range>(ranges)).when(DataTransferObject.class, "toSortedSet", any());
	}

	private void mockLazyLoading() {
		Whitebox.setInternalState(dto, "readingGroupId", 42L);
		BaseTool baseTool = loadFixture("inexistent", BaseTool.class);
		ReadingGroup readingGroup = Whitebox.getInternalState(entity, "readingGroup");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(BaseToolDao.class, "get", "Inexistent")).thenReturn(baseTool);
		when(DaoLazyLoader.createProxy(ReadingGroupDao.class, "get", 42L)).thenReturn(readingGroup);
	}

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("code", String.class, true);
		assertElement("metric", MetricXmlRequest.class, true);
		assertElement("baseToolName", String.class);
		assertElement("weight", Double.class, true);
		assertElement("aggregationForm", Statistic.class, true);
		assertElement("readingGroupId", Long.class);
		assertCollection("range");
	}

	@Test
	public void shouldReturnNullBaseToolForCompoundMetricConfiguration() {
		assertNull(new MetricConfigurationXmlRequest(new MetricConfiguration()).baseTool());
	}

	@Test
	public void shouldConvertNullRangesIntoEmptyCollection() {
		assertTrue(new MetricConfigurationXmlRequest().ranges().isEmpty());
	}

	@Test
	public void checkNullReadingGroup() {
		assertNull(new MetricConfigurationXmlRequest().readingGroup());
		verifyStatic(never());
		DaoLazyLoader.createProxy(eq(ReadingGroupDao.class), anyString(), any());
	}
}