package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.REGULAR;

import java.beans.PropertyVetoException;

import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Range;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(InternalFrame.class)
public class InternalFrameTest extends TestCase {

	private Range range;
	private RangeFrame frame;

	@Before
	public void setUp() {
		range = newRange("amloc", REGULAR);
		frame = new RangeFrame(range);
	}

	@Test
	public void checkTitle() {
		assertEquals("Range " + range, frame.getTitle());
	}

	@Test
	public void shouldBeResizable() {
		assertTrue(frame.isResizable());
	}

	@Test
	public void shouldBeClosable() {
		assertTrue(frame.isClosable());
	}

	@Test
	public void shouldBeMaximizable() {
		assertTrue(frame.isMaximizable());
	}

	@Test
	public void shouldBeIconifiable() {
		assertTrue(frame.isIconifiable());
	}

	@Test
	public void shouldDoNothingOnCloseByDefault() {
		assertEquals(WindowConstants.DO_NOTHING_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test
	public void checkName() {
		assertEquals("range", frame.getName());
	}

	@Test
	public void shouldRetrieveEntity() {
		assertSame(range, frame.get());
	}

	@Test
	public void shouldSelectHidingPropertyVetoException() throws Exception {
		frame = PowerMockito.spy(frame);
		frame.select();
		Mockito.verify(frame).setSelected(true);

		PowerMockito.doThrow(new PropertyVetoException("", null)).when(frame).setSelected(true);
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				frame.select();
			}
		}, "Could not select range frame: " + range, PropertyVetoException.class);
	}
}