package org.kalibro.desktop.swingextension.icon;

import static org.junit.Assert.assertEquals;

import javax.swing.JInternalFrame;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class IconTest extends TestCase {

	private static final int WIDTH = 42;
	private static final int HEIGHT = 36;

	private Icon icon, oldIcon;

	@Before
	public void setUp() {
		icon = new Icon(Icon.KALIBRO);
		oldIcon = PowerMockito.mock(Icon.class);
		PowerMockito.when(oldIcon.getIconWidth()).thenReturn(WIDTH);
		PowerMockito.when(oldIcon.getIconHeight()).thenReturn(HEIGHT);
	}

	@Test
	public void shouldNotAcceptInvalidResource() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				icon = new Icon("inexistent.gif");
			}
		}, NullPointerException.class);
	}

	@Test
	public void shouldScaleForSize() {
		icon = icon.scaleForSize(WIDTH, HEIGHT);
		assertEquals(WIDTH, icon.getIconWidth());
		assertEquals(HEIGHT, icon.getIconHeight());
	}

	@Test
	public void shouldReplaceInternalFrameIconScalingForSameSize() {
		JInternalFrame frame = PowerMockito.mock(JInternalFrame.class);
		PowerMockito.when(frame.getFrameIcon()).thenReturn(oldIcon);
		icon.replaceIconOf(frame);

		ArgumentCaptor<Icon> captor = ArgumentCaptor.forClass(Icon.class);
		Mockito.verify(frame).setFrameIcon(captor.capture());
		icon = captor.getValue();
		assertEquals(WIDTH, icon.getIconWidth());
		assertEquals(HEIGHT, icon.getIconHeight());
	}
}