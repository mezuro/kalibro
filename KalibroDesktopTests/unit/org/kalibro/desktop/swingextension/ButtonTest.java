package org.kalibro.desktop.swingextension;

import static org.junit.Assert.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.desktop.swingextension.field.FieldSize;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(Button.class)
public class ButtonTest extends UnitTest {

	private static final String NAME = "ButtonTest name";
	private static final String TEXT = "ButtonTest text";
	private static final Object TARGET = new Object();
	private static final String METHOD_NAME = "ButtonTest method name";

	private ReflectiveAction action;
	private ActionListener listener;

	private Button button;

	@Before
	public void setUp() throws Exception {
		action = mock(ReflectiveAction.class);
		listener = mock(ActionListener.class);
		whenNew(ReflectiveAction.class).withArguments(TARGET, METHOD_NAME).thenReturn(action);

		button = new Button(NAME, TEXT, listener);
	}

	@Test
	public void shouldSetNameAndText() {
		assertEquals(NAME, button.getName());
		assertEquals(TEXT, button.getText());
	}

	@Test
	public void shouldHaveBoldFont() {
		assertEquals(Font.BOLD, button.getFont().getStyle());
	}

	@Test
	public void shouldHaveCenterHorizontalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getHorizontalAlignment());
	}

	@Test
	public void shouldHaveCenterVerticalAlignment() {
		assertEquals(SwingConstants.CENTER, button.getVerticalAlignment());
	}

	@Test
	public void shouldHaveFieldSize() {
		assertEquals(new FieldSize(button), button.getSize());
	}

	@Test
	public void shouldNotifyListener() {
		button.doClick();
		Mockito.verify(listener).actionPerformed(any(ActionEvent.class));
	}

	@Test
	public void shouldSupportReflectiveAction() {
		button = new Button(NAME, TEXT, TARGET, METHOD_NAME);
		assertEquals(NAME, button.getName());
		assertEquals(TEXT, button.getText());
		assertSame(action, button.getAction());
	}
}