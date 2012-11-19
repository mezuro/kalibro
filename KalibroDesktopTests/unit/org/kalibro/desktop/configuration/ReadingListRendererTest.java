package org.kalibro.desktop.configuration;

import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.RendererUtil;
import org.kalibro.desktop.swingextension.table.DefaultRenderer;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest({ReadingListRenderer.class, RendererUtil.class})
public class ReadingListRendererTest extends UnitTest {

	private Component component, renderedComponent;
	private Boolean isSelected;
	private Reading reading;

	private ReadingListRenderer renderer;

	@Before
	public void setUp() throws Exception {
		mockStatic(RendererUtil.class);
		renderer = new ReadingListRenderer();
		render();
	}

	private void render() throws Exception {
		Random random = new Random();

		reading = mock(Reading.class);
		component = mock(Component.class);
		isSelected = random.nextBoolean();

		int index = random.nextInt();
		boolean hasFocus = random.nextBoolean();
		Color color = new Color(random.nextInt());
		JList<Reading> list = mock(JList.class);

		DefaultRenderer defaultRenderer = mock(DefaultRenderer.class);
		whenNew(DefaultRenderer.class).withNoArguments().thenReturn(defaultRenderer);
		when(defaultRenderer.render(reading)).thenReturn(component);
		when(reading.getColor()).thenReturn(color);

		renderedComponent = renderer.getListCellRendererComponent(list, reading, index, isSelected, hasFocus);
	}

	@Test
	public void shouldRenderWithDefaultRenderer() {
		assertSame(component, renderedComponent);
	}

	@Test
	public void shouldSetBackground() {
		verify(component).setBackground(reading.getColor());
		verifyStatic();
		RendererUtil.setSelectionBackground(component, isSelected);
	}
}