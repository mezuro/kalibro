package org.kalibro.desktop.swingextension;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class LabelManualTest extends JPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("Label", new LabelManualTest()).setVisible(true);
	}

	private LabelManualTest() {
		super(new GridLayout());
		add(new Label("Label created by LabelManualTest"));
	}
}