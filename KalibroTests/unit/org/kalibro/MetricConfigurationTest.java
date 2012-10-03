package org.kalibro;

import static org.junit.Assert.*;

import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.dao.RangeDao;
import org.kalibro.dao.ReadingGroupDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class MetricConfigurationTest extends UnitTest {

	private MetricConfigurationDao dao;

	private MetricConfiguration metricConfiguration;

	@Before
	public void setUp() {
		dao = mock(MetricConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getMetricConfigurationDao()).thenReturn(dao);
		metricConfiguration = new MetricConfiguration();
	}

	@Test
	public void shouldSortByCode() {
		assertSorted(withCode("amloc"), withCode("dit"), withCode("loc"), withCode("nom"), withCode("rfc"));
	}

	@Test
	public void shouldIdentifyByCode() {
		assertEquals(metricConfiguration, withCode(metricConfiguration.getCode()));
	}

	@Test
	public void checkCompoundConstruction() {
		assertEquals("newMetric", metricConfiguration.getCode());
		assertEquals(new CompoundMetric(), metricConfiguration.getMetric());
		assertNull(metricConfiguration.getBaseTool());
		checkConstruction();
	}

	@Test
	public void checkNativeConstruction() {
		BaseTool baseTool = mock(BaseTool.class);
		NativeMetric metric = loadFixture("lcom4", NativeMetric.class);
		metricConfiguration = new MetricConfiguration(baseTool, metric);
		assertEquals("lackOfCohesionOfMethods", metricConfiguration.getCode());
		assertSame(metric, metricConfiguration.getMetric());
		assertSame(baseTool, metricConfiguration.getBaseTool());
		checkConstruction();
	}

	private void checkConstruction() {
		assertFalse(metricConfiguration.hasId());
		assertDoubleEquals(1.0, metricConfiguration.getWeight());
		assertEquals(Statistic.AVERAGE, metricConfiguration.getAggregationForm());
		assertNull(metricConfiguration.getReadingGroup());
		assertTrue(metricConfiguration.getRanges().isEmpty());
	}

	@Test
	public void shouldNotSetConflictingCode() {
		Configuration configuration = new Configuration();
		configuration.addMetricConfiguration(metricConfiguration);
		configuration.addMetricConfiguration(withCode("code"));
		metricConfiguration.setCode("original");
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				metricConfiguration.setCode("code");
			}
		}).throwsException().withMessage("Metric with code 'code' already exists in the configuration.");
		assertEquals("original", metricConfiguration.getCode());
	}

	@Test
	public void shouldAssertNoConflictWithOtherMetricConfiguration() {
		metricConfiguration.assertNoConflictWith(withCode("code"));
	}

	private MetricConfiguration withCode(String code) {
		MetricConfiguration newConfiguration = new MetricConfiguration();
		newConfiguration.setCode(code);
		return newConfiguration;
	}

	@Test
	public void shouldSetConfigurationOnRanges() {
		Range range = mock(Range.class);
		metricConfiguration.setRanges(asSortedSet(range));
		assertDeepEquals(asSet(range), metricConfiguration.getRanges());
		verify(range).setConfiguration(metricConfiguration);
	}

	@Test
	public void shouldSetRangesWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<Range> ranges = mock(SortedSet.class);
		metricConfiguration.setRanges(ranges);
		verifyZeroInteractions(ranges);
	}

	@Test
	public void shouldGetRangeForValue() {
		Range range = mock(Range.class);
		when(range.contains(42.0)).thenReturn(true);
		metricConfiguration.setRanges(asSortedSet(range));

		assertNull(metricConfiguration.getRangeFor(0.0));
		assertSame(range, metricConfiguration.getRangeFor(42.0));
	}

	@Test
	public void shouldAddRangeIfItDoesNotConflictWithExistingOnes() {
		metricConfiguration.addRange(new Range(0.0, 5.0));
		metricConfiguration.addRange(new Range(5.0, 10.0));
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				metricConfiguration.addRange(new Range(4.0, 6.0));
			}
		}).throwsException().withMessage("New range [4.0, 6.0[ would conflict with [0.0, 5.0[");
	}

	@Test
	public void shouldRemoveRange() {
		Range range = mock(Range.class);
		SortedSet<Range> ranges = spy(asSortedSet(range));
		metricConfiguration.setRanges(ranges);

		metricConfiguration.removeRange(range);
		verify(ranges).remove(range);
		verify(range).setConfiguration(null);
	}

	@Test
	public void shouldRequiredCodeAndSavedConfigurationToSave() {
		metricConfiguration.setCode(" ");
		saveShouldThrowExceptionWithMessage("Metric configuration requires code.");

		metricConfiguration.setCode("code");
		saveShouldThrowExceptionWithMessage("Metric is not in any configuration.");

		setConfigurationWithId(null);
		saveShouldThrowExceptionWithMessage("Configuration is not saved. Save configuration instead");
	}

	private void saveShouldThrowExceptionWithMessage(String message) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				metricConfiguration.save();
			}
		}).throwsException().withMessage(message);
	}

	@Test
	public void shouldUpdateIdReadingGroupAndRangesOnSave() {
		Long id = mock(Long.class);
		Long configurationId = mock(Long.class);
		ReadingGroup readingGroup = mockReadingGroup(id);
		Range range = mockRange(id);
		setConfigurationWithId(configurationId);
		when(dao.save(metricConfiguration, configurationId)).thenReturn(id);

		assertFalse(metricConfiguration.hasId());
		metricConfiguration.save();
		assertSame(id, metricConfiguration.getId());
		assertSame(readingGroup, metricConfiguration.getReadingGroup());
		assertEquals(asSet(range), metricConfiguration.getRanges());
	}

	private ReadingGroup mockReadingGroup(Long id) {
		ReadingGroup readingGroup = mock(ReadingGroup.class);
		ReadingGroupDao readingGroupDao = mock(ReadingGroupDao.class);
		when(DaoFactory.getReadingGroupDao()).thenReturn(readingGroupDao);
		when(readingGroupDao.readingGroupOf(id)).thenReturn(readingGroup);
		return readingGroup;
	}

	private Range mockRange(Long id) {
		Range range = mock(Range.class);
		RangeDao rangeDao = mock(RangeDao.class);
		when(DaoFactory.getRangeDao()).thenReturn(rangeDao);
		when(rangeDao.rangesOf(id)).thenReturn(asSortedSet(range));
		return range;
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(metricConfiguration.hasId());
		metricConfiguration.delete();
		verify(dao, never()).delete(any(Long.class));

		Long id = mock(Long.class);
		Whitebox.setInternalState(metricConfiguration, "id", id);
		assertTrue(metricConfiguration.hasId());

		metricConfiguration.delete();
		verify(dao).delete(id);
		assertFalse(metricConfiguration.hasId());
	}

	@Test
	public void shouldRemoveFromConfigurationOnDelete() {
		Configuration configuration = setConfigurationWithId(42L);
		metricConfiguration.delete();
		verify(configuration).removeMetricConfiguration(metricConfiguration);
	}

	private Configuration setConfigurationWithId(Long id) {
		Configuration configuration = mock(Configuration.class);
		when(configuration.hasId()).thenReturn(id != null);
		when(configuration.getId()).thenReturn(id);
		metricConfiguration.setConfiguration(configuration);
		return configuration;
	}

	@Test
	public void shouldNotifyRangesOfDeletion() {
		Range range = mock(Range.class);
		metricConfiguration.setRanges(asSortedSet(range));
		setConfigurationWithId(42L);

		metricConfiguration.delete();
		verify(range).deleted();
	}
}