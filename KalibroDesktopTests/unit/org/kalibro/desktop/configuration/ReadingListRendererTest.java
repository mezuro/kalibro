package org.kalibro.desktop.configuration;

import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.RendererUtil;
import org.kalibro.desktop.swingextension.field.FieldSize;
import org.kalibro.desktop.swingextension.table.DefaultRenderer;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest({ReadingListRenderer.class, RendererUtil.class})
public class ReadingListRendererTest extends UnitTest {

	private static final Random RANDOM = new Random();

	private DefaultRenderer defaultRenderer;
	private Component component;
	private FieldSize fieldSize;
	private Boolean isSelected;
	private Reading reading;

	private ReadingListRenderer renderer;

	@Before
	public void setUp() throws Exception {
		mockStatic(RendererUtil.class);
		createMocks();
		renderer = new ReadingListRenderer();
	}

	private void createMocks() throws Exception {
		reading = mock(Reading.class);
		component = mock(Component.class);
		fieldSize = mock(FieldSize.class);
		isSelected = RANDOM.nextBoolean();
		defaultRenderer = mock(DefaultRenderer.class);

		whenNew(DefaultRenderer.class).withNoArguments().thenReturn(defaultRenderer);
		when(defaultRenderer.render(any())).thenReturn(component);
		whenNew(FieldSize.class).withArguments(component).thenReturn(fieldSize);
		when(reading.getColor()).thenReturn(new Color(RANDOM.nextInt()));
	}

	@Test
	public void shouldRenderWithDefaultRenderer() {
		assertSame(component, render(reading));
		verify(defaultRenderer).render(reading);
	}

	@Test
	public void shouldSetBackground() {
		render(reading);
		verify(component).setBackground(reading.getColor());
		verifyStatic();
		RendererUtil.setSelectionBackground(component, isSelected);
	}

	@Test
	public void shouldRenderNull() {
		assertSame(component, render(null));
		verify(defaultRenderer).render("<none>");
	}

	@Test
	public void shouldRenderWithFieldSize() {
		render(reading);
		verify(component).setSize(fieldSize);
	}

	private Component render(Reading value) {
		return renderer.getListCellRendererComponent(null, value, RANDOM.nextInt(), isSelected, RANDOM.nextBoolean());
	}
}