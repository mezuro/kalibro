package org.kalibro.desktop.reading;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.tests.UnitTest;

public class ReadingGroupControllerTest extends UnitTest {

	private ReadingGroupController controller;

	@Before
	public void setUp() {
		controller = new ReadingGroupController();
	}

	@Test
	public void shouldGetEntityClass() {
		assertEquals(ReadingGroup.class, controller.entityClass());
	}

	@Test
	public void shouldGetPanelForGroup() {
		ReadingGroup group = new ReadingGroup();
		assertSame(group, controller.panelFor(group).get());
	}
}