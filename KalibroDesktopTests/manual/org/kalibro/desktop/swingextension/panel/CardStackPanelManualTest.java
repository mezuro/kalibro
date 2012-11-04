package org.kalibro.desktop.swingextension.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class CardStackPanelManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("CardStackPanel", new CardStackPanelManualTest()).setVisible(true);
	}

	private int panels;
	private CardStackPanel cardStack;
	private Button pushButton, popButton;

	private CardStackPanelManualTest() {
		super(new BorderLayout());
		cardStack = new CardStackPanel();
		pushButton = new Button("", "Push", this);
		popButton = new Button("", "Pop", this);
		add(cardStack, BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		panels = 0;
		push();
	}

	private JPanel southPanel() {
		JPanel southPanel = new JPanel();
		southPanel.add(pushButton);
		southPanel.add(popButton);
		return southPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == pushButton)
			push();
		else if (event.getSource() == popButton)
			pop();
	}

	private void push() {
		cardStack.push(new StringField("" + panels, 30));
		panels++;
	}

	private void pop() {
		cardStack.pop();
		panels--;
	}
}