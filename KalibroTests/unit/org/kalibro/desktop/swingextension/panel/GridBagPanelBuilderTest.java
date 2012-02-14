package org.kalibro.desktop.swingextension.panel;

import static java.awt.GridBagConstraints.*;
import static org.junit.Assert.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.swingextension.field.StringField;

public class GridBagPanelBuilderTest extends KalibroTestCase {

	private JPanel panel;
	private Random random;
	private GridBagLayout layout;
	private GridBagPanelBuilder builder;

	@Before
	public void setUp() {
		random = new Random(System.currentTimeMillis());
		builder = new GridBagPanelBuilder();
		panel = builder.getPanel();
		layout = (GridBagLayout) panel.getLayout();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void iPadShouldBeZero() {
		assertEquals(0, addComponentAndRetrieveConstraints().ipadx);
		assertEquals(0, addComponentAndRetrieveConstraints().ipady);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void insetsShouldBeFive() {
		assertEquals(5, addComponentAndRetrieveConstraints().insets.top);
		assertEquals(5, addComponentAndRetrieveConstraints().insets.bottom);
		assertEquals(5, addComponentAndRetrieveConstraints().insets.right);
		assertEquals(5, addComponentAndRetrieveConstraints().insets.left);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void fillShouldBeBoth() {
		assertEquals(BOTH, addComponentAndRetrieveConstraints().fill);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void anchorShouldBeCenter() {
		assertEquals(CENTER, addComponentAndRetrieveConstraints().anchor);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaulWeightShouldBeZero() {
		assertEquals(0.0, addComponentAndRetrieveConstraints().weightx, 0.0);
		assertEquals(0.0, addComponentAndRetrieveConstraints().weighty, 0.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaulHeightShouldBeOne() {
		assertEquals(1, addComponentAndRetrieveConstraints().gridheight);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaulWidthShouldBeOne() {
		assertEquals(1, addComponentAndRetrieveConstraints().gridwidth);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkLineReset() {
		assertEquals(0, addComponentAndRetrieveConstraints().gridx);
		assertEquals(1, addComponentAndRetrieveConstraints().gridx);
		builder.newLine();
		assertEquals(0, addComponentAndRetrieveConstraints().gridx);
		assertEquals(1, addComponentAndRetrieveConstraints().gridx);
	}

	private GridBagConstraints addComponentAndRetrieveConstraints() {
		addRandomComponent();
		return getLastComponentConstraints();
	}

	private void addRandomComponent() {
		JComponent components[] = new JComponent[]{new Label(""), new StringField("", 5), new DoubleField("")};
		JComponent component = components[random.nextInt(components.length)];
		builder.add(component);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void weightedComponentShouldHaveSameWeightXandY() {
		builder.add(new JLabel(), 4.2);
		assertEquals(4.2, getLastComponentConstraints().weightx, 0.0);
		assertEquals(4.2, getLastComponentConstraints().weighty, 0.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void weightedComponentShouldHaveOneForWidthAndHeight() {
		builder.add(new JLabel(), 4.2);
		assertEquals(1, addComponentAndRetrieveConstraints().gridheight);
		assertEquals(1, addComponentAndRetrieveConstraints().gridwidth);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkHorizontalJumps() {
		builder.add(new JLabel());
		assertEquals(0, getLastComponentConstraints().gridx);

		builder.add(new JLabel(), 2);
		assertEquals(1, getLastComponentConstraints().gridx);

		builder.add(new JLabel(), 3);
		assertEquals(3, getLastComponentConstraints().gridx);

		builder.add(new JLabel());
		assertEquals(6, getLastComponentConstraints().gridx);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkVerticalJumps() {
		builder.add(new JLabel());
		assertEquals(0, getLastComponentConstraints().gridy);

		builder.newLine();
		builder.add(new JLabel(), 1, 2);
		assertEquals(1, getLastComponentConstraints().gridy);

		builder.newLine();
		builder.add(new JLabel(), 1, 3);
		assertEquals(3, getLastComponentConstraints().gridy);

		builder.newLine();
		builder.add(new JLabel());
		assertEquals(6, getLastComponentConstraints().gridy);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoLineJumps() {
		builder.add(new JLabel());
		builder.add(new JLabel(), 1, 3, false);
		builder.newLine();
		builder.add(new JLabel());
		assertEquals(1, getLastComponentConstraints().gridy);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSimpleLine() {
		builder.addSimpleLine(new JLabel(), new JLabel(), new JLabel());
		assertEquals(2, getLastComponentConstraints().gridx);
		builder.add(new JLabel());
		assertEquals(1, getLastComponentConstraints().gridy);
	}

	private GridBagConstraints getLastComponentConstraints() {
		return layout.getConstraints(panel.getComponent(panel.getComponentCount() - 1));
	}
}