package org.kalibro.desktop;

import static org.junit.Assert.*;
import static org.kalibro.desktop.KalibroIcons.*;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.Language;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.LanguagePanel;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class KalibroFrameTest extends UnitTest {

	private static final String TITLE = "KalibroFrameTest title";

	private EditPanel<Language> panel;
	private JTabbedPane tabbedPane;

	private KalibroFrame frame;

	@Before
	public void setUp() {
		panel = new LanguagePanel();
		frame = KalibroFrame.getInstance();
		tabbedPane = new ComponentFinder(frame).find("tabbedPane", JTabbedPane.class);
	}

	@After
	public void tearDown() {
		tabbedPane.removeAll();
	}

	@Test
	public void shouldExitWhenClosing() {
		assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test
	public void shouldHaveKalibroIcon() {
		assertEquals(image(KALIBRO), frame.getIconImage());
	}

	@Test
	public void shouldHaveName() {
		assertEquals("kalibroFrame", frame.getName());
	}

	@Test
	public void shouldHaveMinimumSize() {
		assertEquals(new Dimension(900, 700), frame.getMinimumSize());
	}

	@Test
	public void shouldBeMaximized() {
		assertEquals(Frame.MAXIMIZED_BOTH, frame.getExtendedState());
	}

	@Test
	public void shouldHaveMenuBar() {
		JMenuBar menuBar = frame.getJMenuBar();
		assertNotNull(menuBar);
		assertEquals(2, menuBar.getMenuCount());

		assertClassEquals(KalibroMenu.class, menuBar.getMenu(0));
		assertClassEquals(CrudMenu.class, menuBar.getMenu(1));
	}

	@Test
	public void shouldAddEditPanelWithTitleAndScrollPane() {
		frame.addTab(TITLE, panel);
		assertEquals(TITLE, tabbedPane.getTitleAt(0));
		assertClassEquals(JViewport.class, panel.getParent());
		assertClassEquals(JScrollPane.class, panel.getParent().getParent());
		assertSame(tabbedPane, panel.getParent().getParent().getParent());
	}

	@Test
	public void shouldGetSelectedTitle() {
		assertEquals("", frame.getSelectedTitle());
		frame.add(TITLE, new LanguagePanel());
		assertEquals(TITLE, frame.getSelectedTitle());
	}

	@Test
	public void shouldGetSelectedTab() {
		frame.addTab(TITLE, panel);
		assertSame(panel, frame.getSelectedTab());
	}

	@Test
	public void shouldSetSelectedTitle() {
		frame.addTab("-", panel);
		frame.setSelectedTitle(TITLE);
		assertEquals(TITLE, frame.getSelectedTitle());
	}

	@Test
	public void shouldRemoveSelectedTab() {
		frame.addTab(TITLE, panel);
		frame.removeSelectedTab();
		assertEquals(0, tabbedPane.getComponentCount());
	}
}