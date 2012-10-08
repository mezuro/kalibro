package org.kalibro;

import static java.lang.Double.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RangeDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class RangeTest extends UnitTest {

	private RangeDao dao;

	private Range range;

	@Before
	public void setUp() {
		dao = mock(RangeDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRangeDao()).thenReturn(dao);
		range = new Range();
	}

	@Test
	public void shouldSortByBeginning() {
		assertSorted(withBeginning(NEGATIVE_INFINITY), withBeginning(0.0), withBeginning(28.0), withBeginning(496.0));
	}

	@Test
	public void shouldIdentifyByBeginning() {
		assertEquals(range, withBeginning(range.getBeginning()));
	}

	private Range withBeginning(Double beginning) {
		return new Range(beginning, POSITIVE_INFINITY);
	}

	@Test
	public void checkConstruction() {
		assertDoubleEquals(NEGATIVE_INFINITY, range.getBeginning());
		assertDoubleEquals(POSITIVE_INFINITY, range.getEnd());
		assertFalse(range.hasReading());
		assertEquals("", range.getComments());
	}

	@Test
	public void shouldNotCreateInvalidRange() {
		new Range(1.0, 5.0);
		shouldNotCreate(5.0, 5.0);
		shouldNotCreate(5.0, 1.0);
		shouldNotCreate(NaN, 0.0);
		shouldNotCreate(0.0, NaN);
		shouldNotCreate(NaN, NaN);
	}

	private void shouldNotCreate(final Double beginning, final Double end) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new Range(beginning, end);
			}
		}).throwsException().withMessage("[" + beginning + ", " + end + "[ is not a valid range");
	}

	@Test
	public void shouldNotSetInvalidBeginning() {
		range = new Range(1.0, 5.0);
		range.setBeginning(0.0);
		assertThat(setBeginning(6.0)).throwsException().withMessage("[6.0, 5.0[ is not a valid range");
		assertDoubleEquals(0.0, range.getBeginning());
	}

	@Test
	public void shouldNotSetConflictingBeginning() {
		range = new Range(6.0, 10.0);
		MetricConfiguration configuration = new MetricConfiguration();
		configuration.addRange(new Range(0.0, 5.0));
		configuration.addRange(range);

		range.setBeginning(5.0);
		assertThat(setBeginning(4.0)).throwsException().withMessage("Range [4.0, 10.0[ would conflict with [0.0, 5.0[");
		assertDoubleEquals(5.0, range.getBeginning());
	}

	private VoidTask setBeginning(final Double beginning) {
		return new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				range.setBeginning(beginning);
			}
		};
	}

	@Test
	public void shouldNotSetInvalidEnd() {
		range = new Range(0.0, 4.0);
		range.setEnd(5.0);
		assertThat(setEnd(-1.0)).throwsException().withMessage("[0.0, -1.0[ is not a valid range");
		assertDoubleEquals(5.0, range.getEnd());
	}

	@Test
	public void shouldNotSetConflictingEnd() {
		range = new Range(0.0, 4.0);
		MetricConfiguration configuration = new MetricConfiguration();
		configuration.addRange(range);
		configuration.addRange(new Range(5.0, 10.0));

		range.setEnd(5.0);
		assertThat(setEnd(6.0)).throwsException().withMessage("Range [0.0, 6.0[ would conflict with [5.0, 10.0[");
		assertDoubleEquals(5.0, range.getEnd());
	}

	private VoidTask setEnd(final Double end) {
		return new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				range.setEnd(end);
			}
		};
	}

	@Test
	public void shouldAssertNoIntersectionWithOtherRange() {
		new Range(0.0, 1.0).assertNoIntersectionWith(new Range(1.0, 2.0));
		new Range(-1.0, 0.0).assertNoIntersectionWith(new Range(0.0, 1.0));
	}

	@Test
	public void shouldAnswerIfIsFinite() {
		assertFalse(range.isFinite());
		assertFalse(new Range(NEGATIVE_INFINITY, 0.0).isFinite());
		assertFalse(new Range(0.0, POSITIVE_INFINITY).isFinite());
		assertTrue(new Range(0.0, MAX_VALUE).isFinite());
	}

	@Test
	public void shouldAnswerIfContainsValue() {
		assertTrue(range.contains(0.0));
		assertTrue(new Range(0.0, 2.0).contains(1.0));
		assertTrue(new Range(0.0, POSITIVE_INFINITY).contains(MAX_VALUE));

		assertFalse(range.contains(NaN));
		assertFalse(new Range(NEGATIVE_INFINITY, 0.0).contains(MIN_VALUE));
		assertFalse(new Range(0.0, 3.0).contains(3.0));
	}

	@Test
	public void shouldAcceptAnyReading() {
		assertFalse(range.hasReading());

		Reading reading = mock(Reading.class);
		range.setReading(reading);

		assertTrue(range.hasReading());
		assertSame(reading, range.getReading());
	}

	@Test
	public void shouldRequiredSavedConfigurationToSave() {
		saveShouldThrowExceptionWithMessage("Range is not in any configuration.");

		setConfigurationWithId(null);
		saveShouldThrowExceptionWithMessage("Configuration is not saved. Save configuration instead");
	}

	private void saveShouldThrowExceptionWithMessage(String message) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				range.save();
			}
		}).throwsException().withMessage(message);
	}

	@Test
	public void shouldUpdateIdOnSave() {
		Long id = mock(Long.class);
		Long configurationId = mock(Long.class);
		setConfigurationWithId(configurationId);
		when(dao.save(range, configurationId)).thenReturn(id);

		assertFalse(range.hasId());
		range.save();
		assertSame(id, range.getId());
	}

	@Test
	public void shouldDeleteIfHasId() {
		assertFalse(range.hasId());
		range.delete();
		verify(dao, never()).delete(any(Long.class));

		Long id = mock(Long.class);
		Whitebox.setInternalState(range, "id", id);

		assertTrue(range.hasId());
		range.delete();
		verify(dao).delete(id);
		assertFalse(range.hasId());
	}

	@Test
	public void shouldRemoveFromGroupOnDelete() {
		MetricConfiguration configuration = setConfigurationWithId(42L);
		range.delete();
		verify(configuration).removeRange(range);
	}

	private MetricConfiguration setConfigurationWithId(Long id) {
		MetricConfiguration configuration = mock(MetricConfiguration.class);
		when(configuration.hasId()).thenReturn(id != null);
		when(configuration.getId()).thenReturn(id);
		range.setConfiguration(configuration);
		return configuration;
	}
}