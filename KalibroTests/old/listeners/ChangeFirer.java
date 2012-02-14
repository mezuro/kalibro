package org.kalibro.desktop.old.listeners;

import java.awt.event.*;

public class ChangeFirer implements ActionListener, ItemListener, KeyListener {

	private ChangeListener listener;

	public ChangeFirer(ChangeListener listener) {
		this.listener = listener;
	}
	
	private void changed(){
		listener.changed();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		changed();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		// keyTyped is enough
	}

	@Override
	public void keyReleased(KeyEvent event) {
		// keyTyped is enough
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (! event.isConsumed())
			changed();
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		changed();
	}
}